package springbook.user.dao;

import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.List;

public class UserService implements UserLevelUpgradePolicy
{
    UserDao userDao;
    public void setUserDao(UserDao userDao)
    {
        this.userDao = userDao;
    }

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMEND_FOR_GOLD = 30;

    public void upgradeLevels()
    {
        List<User> users = userDao.getAll();
        for(User user : users)
        {
            if (canUpgradeLevel(user))
            {
                upgradeLevel(user);
            }
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
