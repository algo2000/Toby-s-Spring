package springbook.learningtest.spring.ioc.bean;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import java.io.IOException;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class HelloTest
{
    @Test
    public void registerBeanWithDependency()
    {
        /*
        <bean id = "printer" class = "springbook.learningtest.spring.ioc.bean.Hello.StringPrinter"/>

        <bean id = "hello" class = "springbook.learningtest.spring.ioc.bean.Hello">
            <property name = "name" value = "Spring"/>
            <property name = "printer" ref = "printer"/>
        </bean>
         */
        StaticApplicationContext ac = new StaticApplicationContext();
        ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));

        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name","Spring");
        helloDef.getPropertyValues().addPropertyValue("printer",new RuntimeBeanReference("printer"));

        ac.registerBeanDefinition("hello",helloDef);

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString(),is("springbook.learningtest.spring.ioc.bean.Hello.Hello Spring"));
    }

    @Test
    public void genericApplicationContext()
    {
        GenericApplicationContext ac = new GenericXmlApplicationContext(String.valueOf((getClass().getResource("/ApplicationContext.xml"))));

        Hello hello = ac.getBean("hello",Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString(),is("Hello Spring"));
    }
}
