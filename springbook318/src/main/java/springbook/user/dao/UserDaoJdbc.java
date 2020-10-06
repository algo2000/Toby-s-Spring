package springbook.user.dao;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.util.List;

public class UserDaoJdbc implements UserDao
{
    private RowMapper<User> userMapper = ((rs, rowNum)->
    {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setLevel(Level.valueOf(rs.getInt("level"))); //int형태의 값을 level 이눔 형태로 변환
        user.setLogin(rs.getInt("login"));
        user.setRecommend(rs.getInt("recommend"));
        return user;
    });

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource)
    {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user) throws DuplicateUserIdException
    {
        this.jdbcTemplate.update("insert into USER (id,name,password,level, login, recommend)" +
                " values (?,?,?,?,?,?);",user.getId(),user.getName(),user.getPassword(),
                user.getLevel().intValue(), user.getLogin(),user.getRecommend());
    }

    public User get(String id)
    {
        return this.jdbcTemplate.queryForObject("select * from User where id = ?", new Object[]{id}, this.userMapper);
    }

    public void deleteAll()
    {
        this.jdbcTemplate.update("delete from USER");
    }

    public int getCount()
    {
        return this.jdbcTemplate.queryForObject("select count(*) from User",Integer.class);
    }

    public List<User> getAll()
    {
        return this.jdbcTemplate.query("select * from User order by id", this.userMapper);
    }
}
