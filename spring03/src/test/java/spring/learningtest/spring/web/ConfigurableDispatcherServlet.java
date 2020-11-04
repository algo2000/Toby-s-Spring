package spring.learningtest.spring.web;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ConfigurableDispatcherServlet extends DispatcherServlet
{
    private Class<?>[] classes;
    private String[] locations;

    private ModelAndView modelAndView; //컨트롤러가 디스페처 서블릿에 돌려주는 모델과 뷰 정보 저장

    public ConfigurableDispatcherServlet(String[] locations)
    {
        this.locations = locations;
    }

    public ConfigurableDispatcherServlet(Class<?> ...classes)
    {
        this.classes = classes;
    }

    public void setLocations(String ...locations)
    {
        this.locations = locations;
    }

    public void setClasses(Class<?> ...classes)
    {
        this.classes = classes;
    }

    //주어진 클래스로부터 상대적인 위치의 클래스 패스에 있는 설정 파일을 지정할 수 있게 해줌
    public void setRelativeLocations(Class clazz,String ...relativeLocations)
    {
        String[] locations = new String[relativeLocations.length];
        String currentPath = "";

        for(int i = 0; i<relativeLocations.length; i++)
        {
            locations[i] = currentPath + relativeLocations[i];
        }
        this.setLocations(locations);
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
    {
        modelAndView = null;
        super.service(req, res);
    }

    @Override
    protected WebApplicationContext createWebApplicationContext(ApplicationContext parent)
    {
        AbstractRefreshableWebApplicationContext wac = new AbstractRefreshableWebApplicationContext()
        {
            @Override
            protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException
            {
                if(locations != null)
                {
                    XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(beanFactory);
                    xmlReader.loadBeanDefinitions(locations);
                }
                if(classes != null)
                {
                    AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory);
                    reader.register(classes);
                }
            }
        };

        wac.setServletContext(getServletContext());
        wac.setServletConfig(getServletConfig());
        wac.refresh();

        return wac;
    }

    @Override
    protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        this.modelAndView = mv;
        super.render(mv, request, response);
    }

    public ModelAndView getModelAndView()
    {
        return modelAndView;
    }
}
