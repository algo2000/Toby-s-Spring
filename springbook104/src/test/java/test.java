import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.sql.SQLException;

public class test
{
    @Test
    public void test() throws SQLException, ClassNotFoundException
    {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);  //가져올 컨텍스트.class
        UserDao dao = context.getBean("userDao",UserDao.class); //메소드 이름, 리턴 타입

        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());

        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }

    @Test
    public void singleToneTest1()
    {
        DaoFactory factory = new DaoFactory();
        UserDao dao1 = factory.userDao();
        UserDao dao2 = factory.userDao();

        System.out.println("일반 컨텍스트");
        System.out.println(dao1);
        System.out.println(dao2);
        System.out.println();

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);  //가져올 컨텍스트.class
        UserDao dao3 = context.getBean("userDao",UserDao.class); //메소드 이름, 리턴 타입
        UserDao dao4 = context.getBean("userDao",UserDao.class); //메소드 이름, 리턴 타입

        System.out.println("스프링 컨텍스트");
        System.out.println(dao3);
        System.out.println(dao4);
        System.out.println();
    }
}
