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

public class UserServiceImpl implements UserService
{
    private UserDao userDao;
    private MailSender mailSender;

    public void setUserDao(UserDao userDao)
    {
        this.userDao = userDao;
    }
    public void setMailSender(MailSender mailSender)
    {
        this.mailSender = mailSender;
    }

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMEND_FOR_GOLD = 30;

    @Override
    public void upgradeLevels()
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

    @Override
    public void add(User user)
    {
        if (user.getLevel() == null)
        {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

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
}
