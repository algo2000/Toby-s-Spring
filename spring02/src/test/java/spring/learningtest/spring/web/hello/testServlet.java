package spring.learningtest.spring.web.hello;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.servlet.ModelAndView;
import spring.learningtest.spring.web.ConfigurableDispatcherServlet;
import spring.temp.HelloController;
import spring.temp.HelloSpring;
import spring.temp.SimpleGetServlet;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class testServlet
{
    @Test
    public void testRequest() throws ServletException, IOException
    {
        MockHttpServletRequest req = new MockHttpServletRequest("GET","/hello");
        req.addParameter("name","Spring");

        MockHttpServletResponse res = new MockHttpServletResponse();

        SimpleGetServlet servlet = new SimpleGetServlet();
        servlet.service(req,res);

        assertThat(res.getContentAsString(), is("<html><body>Hello Spring</body></html>"));

        assertThat(res.getContentAsString().contains("Hello Spring"), is(true));
    }

    @Test
    public void testServlet() throws ServletException, IOException
    {
        ConfigurableDispatcherServlet servlet = new ConfigurableDispatcherServlet();
        servlet.setRelativeLocations(getClass(), "dispatcher-servlet.xml");
        servlet.setClasses(HelloSpring.class);
        servlet.init(new MockServletConfig("dispatcher"));

        MockHttpServletRequest req = new MockHttpServletRequest("GET","/hello");
        req.addParameter("name","Spring");
        MockHttpServletResponse res = new MockHttpServletResponse();
        servlet.service(req,res);

        ModelAndView mav = servlet.getModelAndView();
        assertThat(mav.getViewName(), is("/hello"));
        assertThat((String)mav.getModel().get("message"),is("Hello Spring"));
    }

    @Test
    public void testServlet2() throws Exception
    {
        ApplicationContext ac = new GenericXmlApplicationContext("dispatcher-servlet.xml", "applicationContext.xml");
        HelloController helloController = ac.getBean(HelloController.class);

        MockHttpServletRequest req = new MockHttpServletRequest("GET","/hello");
        req.addParameter("name","Spring");
        MockHttpServletResponse res = new MockHttpServletResponse();

        ModelAndView mav = helloController.handleRequest(req,res);
        assertThat(mav.getViewName(), is("/hello"));
        assertThat((String)mav.getModel().get("message"),is("Hello Spring"));
    }
}
