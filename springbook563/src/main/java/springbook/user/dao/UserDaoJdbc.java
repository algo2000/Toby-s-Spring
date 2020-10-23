package springbook.user.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.service.DuplicateUserIdException;
import springbook.user.sqlservice.SqlService;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class UserDaoJdbc implements UserDao
{
    private SqlService sqlService;

    public void setSqlService(SqlService sqlService)
    {
        this.sqlService = sqlService;
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
        this.jdbcTemplate.update(this.sqlService.getSql("userAdd"),user.getId(),user.getName(),user.getPassword(),
                user.getLevel().intValue(), user.getLogin(),user.getRecommend(),user.getEmail());
    }

    @Override
    public User get(String id)
    {
        return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGet"), new Object[]{id}, this.userMapper);
    }

    @Override
    public void deleteAll()
    {
        this.jdbcTemplate.update(this.sqlService.getSql("userDeleteAll"));
    }

    @Override
    public int getCount()
    {
        return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGetCount"),Integer.class);
    }

    @Override
    public void update(User user)
    {
        this.jdbcTemplate.update(
            this.sqlService.getSql("userUpdate"),
                user.getName(), user.getPassword(), user.getLevel().intValue(),user.getLogin(),
                user.getRecommend(), user.getEmail(), user.getId());
    }

    @Override
    public List<User> getAll()
    {
        return this.jdbcTemplate.query(this.sqlService.getSql("userGetAll"), this.userMapper);
    }
}
