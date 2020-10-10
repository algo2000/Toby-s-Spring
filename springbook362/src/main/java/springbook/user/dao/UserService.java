package springbook.user.dao;

import org.springframework.jdbc.datasource.DataSourceUtils;
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
        TransactionSynchronizationManager.initSynchronization(); //트랜잭션 동기화 관리자를 이용하여 동기화 작업 초기화
        Connection c = DataSourceUtils.getConnection(dataSource); //DB커넥션 생성 트랜잭션 생성 이후의 DAO작업은 모두 여기서 시작한 트랜잭션 안에서 진행

        c.setAutoCommit(false);

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
            c.commit();
        }
        catch (Exception e)
        {
            c.rollback();
            throw e;
        }
        finally
        {
            DataSourceUtils.releaseConnection(c,dataSource); //스프링 유틸리티 메소드를 이요하여 DB컨넥션을 닫음
            TransactionSynchronizationManager.unbindResource(this.dataSource); //동기화 작업
            TransactionSynchronizationManager.clearSynchronization(); //동기화 종료
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
