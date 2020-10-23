package springbook.user.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.service.DuplicateUserIdException;

import javax.sql.DataSource;
import java.util.List;

public class UserDaoJdbc implements UserDao
{
    private String sqlAdd;

    public void setSqlAdd(String sqlAdd)
    {
        this.sqlAdd = sqlAdd;
    }

    private RowMapper<User> userMapper = ((rs, rowNum)->
    {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setLevel(Level.valueOf(rs.getInt("level"))); //int형태의 값을 level 이눔 형태로 변환
        user.setLogin(rs.getInt("login"));
        user.setRecommend(rs.getInt("recommend"));
        user.setEmail(rs.getString("email"));
        return user;
    });

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource)
    {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(final User user) throws DuplicateUserIdException
    {
        this.jdbcTemplate.update(this.sqlAdd,user.getId(),user.getName(),user.getPassword(),
                user.getLevel().intValue(), user.getLogin(),user.getRecommend(),user.getEmail());
    }

    @Override
    public User get(String id)
    {
        return this.jdbcTemplate.queryForObject("select * from User where id = ?", new Object[]{id}, this.userMapper);
    }

    @Override
    public void deleteAll()
    {
        this.jdbcTemplate.update("delete from USER");
    }

    @Override
    public int getCount()
    {
        return this.jdbcTemplate.queryForObject("select count(*) from User",Integer.class);
    }

    @Override
    public void update(User user)
    {
        this.jdbcTemplate.update(
            "update USER set name = ?, password=?, level=?, login=?, recommend=?,email=? where id = ?",
                user.getName(), user.getPassword(), user.getLevel().intValue(),user.getLogin(),
                user.getRecommend(), user.getEmail(), user.getId());
    }

    @Override
    public List<User> getAll()
    {
        return this.jdbcTemplate.query("select * from User order by id", this.userMapper);
    }
}
