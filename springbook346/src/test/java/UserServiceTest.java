import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.dao.UserService;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext.xml")
public class UserServiceTest
{
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMEND_FOR_GOLD = 30;

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    List<User> users;

    @Before
    public void setUp()
    {
        users = Arrays.asList(
                new User("a","에이","p1", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER-1,0),
                new User("b","비","p2", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER,0),
                new User("c","씨","p3", Level.SILVER,60,MIN_RECOMEND_FOR_GOLD-1),
                new User("d","디","p4", Level.SILVER,60,MIN_RECOMEND_FOR_GOLD),
                new User("e","이","p5", Level.GOLD,100,Integer.MAX_VALUE)
        );
    }

    @Test
    public void upgradeLevels()
    {
        userDao.deleteAll();
        for (User user : users)
        {
            userDao.add(user);
        }
        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0),false);
        checkLevelUpgraded(users.get(1),true);
        checkLevelUpgraded(users.get(2),false);
        checkLevelUpgraded(users.get(3),true);
        checkLevelUpgraded(users.get(4),false);
    }

    @Test
    public void add()
    {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel(),is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelRead.getLevel(),is(Level.BASIC));
    }

    @Test
    public void bean()
    {
        assertThat(this.userService, is(notNullValue()));
    }

    private void checkLevel(User user, Level expectedLevel)
    {
        User userUpgrade = userDao.get(user.getId());
        assertThat(userUpgrade.getLevel(),is(expectedLevel));
    }

    private void checkLevelUpgraded(User user, boolean upgraded)
    {
        User userUpdate = userDao.get(user.getId());
        if (upgraded)
        {
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        }
        else
        {
            assertThat(userUpdate.getLevel(),is(user.getLevel()));
        }
    }
}
