package springbook.user.dao;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService implements UserLevelUpgradePolicy
{
    private UserDao userDao;
    public void setUserDao(UserDao userDao)
    {
        this.userDao = userDao;
    }

    private DataSource dataSource;
    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMEND_FOR_GOLD = 30;

    public void upgradeLevels() throws Exception
    {
        //JDBC 트랜잭션 추상 오브젝트 생성
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        //트랜잭션 매니저가 DB 커넥션을 가져오는 작업을 같이 수행
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try
        {
            List<User> users = userDao.getAll();
            for(User user : users)
            {
                if (canUpgradeLevel(user))
                {
                    upgradeLevel(user);
                }
            }
            transactionManager.commit(status);  //트랜잭션 커밋
        }
        catch (Exception e)
        {
            transactionManager.rollback(status);    //트랜잭션 커
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
    }
}
