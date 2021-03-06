import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDaoJdbc;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= "/test-applicationContext.xml")
public class UserDaoTest
{
    @Autowired
    private UserDaoJdbc dao;
    @Autowired
    private DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp()
    {
    }

    @Before
    @DirtiesContext //추가시 이전에 사용한 어플리케인션 컨텍스트를 공유하지 않고 새로 만듬
    public void setUpUser()
    {
        user1 = new User("lee1","이종윤","spring", Level.GOLD,1,0);
        user2 = new User("lee2","이종윤","spring",Level.SILVER, 55, 10);
        user3 = new User("lee3","이종윤","spring", Level.GOLD, 100, 40);
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

        User userget1 = dao.get(user1.getId());
        checkSameUser(userget1, user1);

        User userget2 = dao.get(user2.getId());
        checkSameUser(userget2, user2);
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

    @Test
    public void getAll()
    {
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assertThat(users0.size(),is(0));

        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertThat(users1.size(),is(1));
        checkSameUser(user1,users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size(),is(2));
        checkSameUser(user1,users2.get(0));
        checkSameUser(user2,users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size(),is(3));
        checkSameUser(user1,users3.get(0));
        checkSameUser(user2,users3.get(1));
        checkSameUser(user3,users3.get(2));
    }

    @Test(expected = DuplicateKeyException.class)
    public void duplciateKey()
    {
        dao.deleteAll();
        dao.add(user1);
        dao.add(user1);
    }

    @Test
    public void sqlExceptionTranslate()
    {
        dao.deleteAll();
        try
        {
            dao.add(user1);
            dao.add(user1);
        }
        catch (DuplicateKeyException ex)
        {
            SQLException sqlEx = (SQLException)ex.getRootCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);

            assertThat(set.translate(null,null,sqlEx),instanceOf(DuplicateKeyException.class));
        }
    }

    @Test
    public void update()
    {
        dao.deleteAll();
        dao.add(user1);

        user1.setName("오민규");
        user1.setPassword("springno6");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1,user1update);
    }

    private void checkSameUser(User user1, User user2)
    {
        assertThat(user1.getId(),is(user2.getId()));
        assertThat(user1.getName(),is(user2.getName()));
        assertThat(user1.getPassword(),is(user2.getPassword()));
        assertThat(user1.getLevel(),is(user2.getLevel()));
        assertThat(user1.getLogin(),is(user2.getLogin()));
        assertThat(user1.getRecommend(),is(user2.getRecommend()));
    }
}
