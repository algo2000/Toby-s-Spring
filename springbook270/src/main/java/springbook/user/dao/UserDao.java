package springbook.user.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.util.List;

public class UserDao
{
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource)
    {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        this.dataSource = dataSource;
    }

    public void add(final User user)
    {
        this.jdbcTemplate.update("insert into USER (id,name,password) values (?,?,?);",user.getId(),user.getName(),user.getPassword());
    }

    public User get(String id)
    {
        return this.jdbcTemplate.queryForObject("select * from User where id = ?", new Object[]{id}, (rs, rowNum) ->
        {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
        });
    }

    public void deleteAll()
    {
        this.jdbcTemplate.update("delete from USER");
    }

    public int getCount()
    {
//        return this.jdbcTemplate.query(con -> con.prepareStatement("select count(*) from User"), rs ->
//        {
//            rs.next();
//            return rs.getInt(1);
//        });
        return this.jdbcTemplate.queryForObject("select count(*) from User",Integer.class);
    }

    public List<User> getAll()
    {
        return this.jdbcTemplate.query("select * from User order by id", (rs, rowNum)->
        {
           User user = new User();
           user.setId(rs.getString("id"));
           user.setName(rs.getString("name"));
           user.setPassword(rs.getString("password"));
           return user;
        });
    }
}
