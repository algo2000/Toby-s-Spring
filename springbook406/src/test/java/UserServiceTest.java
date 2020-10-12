import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.dao.UserDao;
import springbook.user.service.MockMailSender;
import springbook.user.service.UserServiceImpl;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.service.UserServiceTx;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext.xml")
public class UserServiceTest
{
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMEND_FOR_GOLD = 30;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserDao userDao;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    MailSender mailSender;

    @Autowired UserServiceImpl userServiceImpl;

    List<User> users;

    @Before
    public void setUp()
    {
        users = Arrays.asList(
                new User("a","에이","p1", "algo2000@naver.com", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER-1,0),
                new User("b","비","p2", "algo2000@naver.com", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER,0),
                new User("c","씨","p3", "algo2000@naver.com", Level.SILVER,60,MIN_RECOMEND_FOR_GOLD-1),
                new User("d","디","p4", "algo2000@naver.com", Level.SILVER,60,MIN_RECOMEND_FOR_GOLD),
                new User("e","이","p5", "algo2000@naver.com", Level.GOLD,100,Integer.MAX_VALUE)
        );
    }

    @Test
    @DirtiesContext //컨텍스트의 DI 설정을 변경하는 테스트라는것을 알려줌
    public void upgradeLevels() throws Exception
    {
        userDao.deleteAll();
        for (User user : users)
        {
            userDao.add(user);
        }

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0),false);
        checkLevelUpgraded(users.get(1),true);
        checkLevelUpgraded(users.get(2),false);
        checkLevelUpgraded(users.get(3),true);
        checkLevelUpgraded(users.get(4),false);

        List<String> request = mockMailSender.getRequests();
        assertThat(request.size(),is(2));
        assertThat(request.get(0),is(users.get(1).getEmail()));
        assertThat(request.get(1),is(users.get(3).getEmail()));
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
    public void upgradeAllOrNothing() throws Exception
    {
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        MockMailSender mockMailSender = new MockMailSender();
        testUserService.setMailSender(mockMailSender);

        UserServiceTx txUserService = new UserServiceTx();
        txUserService.setTransactionManager(transactionManager);
        txUserService.setUserService(testUserService);

        userDao.deleteAll();
        for (User user : users)
        {
            userDao.add(user);
        }

        try
        {
            txUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }
        catch (TestUserServiceException e)
        {
        }
        checkLevelUpgraded(users.get(1),false);
    }

    @Test
    public void bean()
    {
        assertThat(this.userService, is(notNullValue()));
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
