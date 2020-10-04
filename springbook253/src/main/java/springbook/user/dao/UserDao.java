package springbook.user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

public class UserDao
{
    private DataSource dataSource;
    private JdbcContext jdbcContext;

    public void setDataSource(DataSource dataSource)
    {
        this.jdbcContext = new JdbcContext();   //jdbcContext 생성(IoC)
        this.jdbcContext.setDataSource(dataSource); //의존 오브젝트 주입(DI)
        this.dataSource = dataSource;   //아직 JdbcContext를 적용하지 않은 메소드를 위해 저장
    }

    public void add(final User user) throws SQLException
    {
        this.jdbcContext.workWithStatementStrategy(c ->
        {
            PreparedStatement ps = c.prepareStatement("insert into USER (id,name,password) values (?,?,?);");

            ps.setString(1,user.getId());
            ps.setString(2,user.getName());
            ps.setString(3,user.getPassword());

            return ps;
        });
    }

    public User get(String id) throws SQLException
    {
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement("select * from User where id = ?");
        ps.setString(1,id);

        ResultSet rs = ps.executeQuery();
        User user = null;

        if(rs.next())
        {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        if(user==null)
        {
            throw new EmptyResultDataAccessException(1);
        }

        return user;
    }

    public void deleteAll() throws SQLException
    {
        this.jdbcContext.executeSql("delete from USER");
    }

    public int getCount() throws SQLException
    {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            c = dataSource.getConnection();
            ps = c.prepareStatement("select count(*) from User");
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            if(rs != null){try { rs.close(); } catch (SQLException e){} }
            if(ps != null){try { ps.close(); } catch (SQLException e){} }
            if(c != null){try { c.close(); } catch (SQLException e){} }
        }
    }
}
