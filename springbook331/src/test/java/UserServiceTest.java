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
    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    List<User> users;

    @Before
    public void setUp()
    {
        users = Arrays.asList(
                new User("a","에이","p1", Level.BASIC,49,0),
                new User("b","비","p2", Level.BASIC,50,0),
                new User("c","씨","p3", Level.SILVER,60,29),
                new User("d","디","p4", Level.SILVER,60,30),
                new User("e","이","p5", Level.GOLD,100,100)
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

        checkLevel(users.get(0),Level.BASIC);
        checkLevel(users.get(1),Level.SILVER);
        checkLevel(users.get(2),Level.SILVER);
        checkLevel(users.get(3),Level.GOLD);
        checkLevel(users.get(4),Level.GOLD);
    }

    @Test
    public void bean()
    {
        assertThat(this.userService, is(notNullValue()));
    }

    public void checkLevel(User user, Level expectedLevel)
    {
        User userUpgrade = userDao.get(user.getId());
        assertThat(userUpgrade.getLevel(),is(expectedLevel));
    }
}
