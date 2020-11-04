package spring.learningtest.spring.web.hello;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import spring.learningtest.spring.web.AbstractDispatcherServletTest;
import spring.temp.HelloSpring;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SimpleHelloControllerTest extends AbstractDispatcherServletTest
{
    @Test
    public void helloController() throws ServletException, IOException
    {
        ModelAndView mav = setRelativeLocations("dispatcher-servlet.xml")
                .setClasses(HelloSpring.class)
                .initRequest("/hello", RequestMethod.GET)
                .addParameter("name","Spring")
                .runService()
                .getModelAndView();

        assertThat(mav.getViewName(),is("/hello"));
        assertThat((String)mav.getModel().get("message"),is("Hello Spring"));
    }
    @Test
    public void helloController2() throws ServletException, IOException
    {
        setRelativeLocations("dispatcher-servlet.xml").setClasses(HelloSpring.class);
        initRequest("/hello","GET").addParameter("name","Spring");
        runService().assertModel("message","Hello Spring").assertViewName("/hello");
    }
}
