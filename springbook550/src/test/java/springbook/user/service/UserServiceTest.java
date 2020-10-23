package springbook.user.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.user.dao.MockUserDao;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext.xml")
public class UserServiceTest
{
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMEND_FOR_GOLD = 30;

    @Autowired UserService userService;
    @Autowired UserDao userDao;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired MailSender mailSender;
    @Autowired UserService testUserService;
    @Autowired ApplicationContext context;


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
    public void advisorAutoProxyCreator()
    {
        assertThat(testUserService, instanceOf(java.lang.reflect.Proxy.class));
    }

    @Test
    public void mockUpgradeLevels() throws Exception
    {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(users.get(1));
        assertThat(users.get(1).getLevel(),is(Level.SILVER));
        verify(mockUserDao).update(users.get(3));
        assertThat(users.get(3).getLevel(),is(Level.GOLD));

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender,times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0],is(users.get(1).getEmail()));
        assertThat(mailMessages.get(1).getTo()[0],is(users.get(3).getEmail()));
    }

    @Test
    public void upgradeLevels()
    {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size(),is(2));
        checkUserAndLevel(updated.get(0), "b", Level.SILVER);
        checkUserAndLevel(updated.get(1), "d", Level.GOLD);

        List<String> request = mockMailSender.getRequests();
        assertThat(request.size(),is(2));
        assertThat(request.get(0),is(users.get(1).getEmail()));
        assertThat(request.get(1),is(users.get(3).getEmail()));
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel)
    {
        assertThat(updated.getId(),is(expectedId));
        assertThat(updated.getLevel(),is(expectedLevel));
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
    @DirtiesContext //다이내믹 프록시 팩토리 빈을 직접 만들어 사용할 때는 없앴다가 다시 등장한 컨텍스트 무효화 애노테이션
    public void upgradeAllOrNothing() throws Exception
    {
        userDao.deleteAll();
        for (User user : users)
        {
            userDao.add(user);
        }

        try
        {
            this.testUserService.upgradeLevels();
            fail("springbook.user.service.TestUserServiceException expected");
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

    @Test(expected = TransientDataAccessResourceException.class)
    public void readOnlyTransactionAttribute()
    {
        testUserService.getAll();
    }

    @Test
    @Transactional
    //@Transactional(readOnly = true) error 발생 메소드의 트랜잭션 속성이 클래스보다 우선 운위
    public void transactionSync()
    {
        userService.deleteAll();
        userService.add(users.get(0));
        userService.add(users.get(1));
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

    static class TestUserService extends UserServiceImpl
    {
        private String id = "d";

        @Override
        public void upgradeLevel(User user)
        {
            if (user.getId().equals(this.id))
            {
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }

        @Override
        public List<User> getAll()
        {
            for (User user : super.getAll())
            {
                super.update(user);
            }
            return null;
        }
    }
}
