package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.*;

public class UserDao
{
    private SimpleConnectionMaker simpleConnectionMaker;

    public UserDao()
    {
        simpleConnectionMaker = new SimpleConnectionMaker();
    }

    public void add(User user) throws ClassNotFoundException, SQLException
    {
        Connection c = simpleConnectionMaker.makeNewConnectionMaker();

        PreparedStatement ps = c.prepareStatement("insert into User(id,name,password) values(?,?,?)");
        ps.setString(1,user.getId());
        ps.setString(2,user.getName());
        ps.setString(3,user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException
    {
        Connection c = simpleConnectionMaker.makeNewConnectionMaker();
        PreparedStatement ps = c.prepareStatement("select * from User where id = ?");
        ps.setString(1,id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }
}
