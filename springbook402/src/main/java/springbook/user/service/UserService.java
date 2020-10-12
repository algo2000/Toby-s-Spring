package springbook.user.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.List;

public class UserService implements UserLevelUpgradePolicy
{
    private UserDao userDao;

    public void setUserDao(UserDao userDao)
    {
        this.userDao = userDao;
    }

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMEND_FOR_GOLD = 30;

    private PlatformTransactionManager transactionManager;

    private MailSender mailSender;

    public void setMailSender(MailSender mailSender)
    {
        this.mailSender = mailSender;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager)
    {
        this.transactionManager = transactionManager;
    }

    public void upgradeLevels() throws Exception
    {
        //트랜잭션 매니저가 DB 커넥션을 가져오는 작업을 같이 수행
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try
        {
            upgradeLevelsInternal();
            this.transactionManager.commit(status);  //트랜잭션 커밋
        }
        catch (Exception e)
        {
            this.transactionManager.rollback(status);    //트랜잭션 커
            throw e;
        }
    }

    public void add(User user)
    {
        if (user.getLevel() == null)
        {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    @Override
    public boolean canUpgradeLevel(User user)
    {
        Level currentLevel = user.getLevel();
        switch (currentLevel)
        {
            case BASIC: return (user.getLogin()>=MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend()>=MIN_RECOMEND_FOR_GOLD);
            case GOLD: return false;
            default:throw new IllegalArgumentException("Unknown Level : "+currentLevel);
        }
    }

    @Override
    public void upgradeLevel(User user)
    {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEMail(user);
    }
    
    private void sendUpgradeEMail(User user)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("muragon2015@gmail.com");
        mailMessage.setSubject("upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name());

        this.mailSender.send(mailMessage);
    }

    private void upgradeLevelsInternal()
    {
        List<User> users = userDao.getAll();
        for (User user : users)
        {
            if (canUpgradeLevel(user))
            {
                upgradeLevel(user);
            }
        }
    }
}
