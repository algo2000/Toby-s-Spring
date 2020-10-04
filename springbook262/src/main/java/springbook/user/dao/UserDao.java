package springbook.user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import springbook.user.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

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

    public User get(String id) throws SQLException
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

    public void deleteAll() throws SQLException
    {
        this.jdbcTemplate.update("delete from USER");
    }

    public int getCount() throws SQLException
    {
        //        return this.jdbcTemplate.query(con -> con.prepareStatement("select count(*) from User"), rs ->
//        {
//            rs.next();
//            return rs.getInt(1);
//        });
        return this.jdbcTemplate.queryForObject("select count(*) from User",Integer.class);
    }
}
