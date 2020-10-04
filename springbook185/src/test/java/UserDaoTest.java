import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserDaoTest
{
    private UserDao dao;
    private User user1;
    private User user2;
    private User user3;

    @Autowired
    private ApplicationContext context;

    @Before
    public void setUp()
    {
        System.out.println("test");
        System.out.println(this.context);
        System.out.println(this+"\n");

        this.dao = this.context.getBean("userDao",UserDao.class);
    }

    @Before
    public void setUpUser()
    {
        user1 = new User("lee1","이종윤","spring");
        user2 = new User("lee2","이종윤","spring");
        user3 = new User("lee3","이종윤","spring");
    }

    @Test
    public void addAndGet() throws SQLException
    {
        dao.deleteAll();
        assertThat(dao.getCount(),is(0));

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(),is(2));

        User userGet1 = dao.get(user1.getId());
        assertThat(userGet1.getName(), is(user1.getName()));
        assertThat(userGet1.getPassword(), is(user1.getPassword()));

        User userGet2 = dao.get(user2.getId());
        assertThat(userGet2.getName(), is(user2.getName()));
        assertThat(userGet2.getPassword(), is(user2.getPassword()));
    }

    @Test
    public void count() throws SQLException
    {
        dao.deleteAll();
        assertThat(dao.getCount(),is(0));

        dao.add(user1);
        assertThat(dao.getCount(),is(1));

        dao.add(user2);
        assertThat(dao.getCount(),is(2));

        dao.add(user3);
        assertThat(dao.getCount(),is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException
    {
        dao.deleteAll();
        assertThat(dao.getCount(),is(0));
        dao.get("unknown_id");
    }
}
