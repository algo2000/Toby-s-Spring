package springbook.learningtest.spring.ioc.bean;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import springbook.learningtest.spring.ioc.annotation.StringPrinter;
import springbook.learningtest.spring.ioc.config.AnnotatedHelloConfig;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

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

    @Test
    public void contextText()
    {
        ApplicationContext parent = new GenericXmlApplicationContext(String.valueOf((getClass().getResource("/parentContext.xml"))));
        GenericApplicationContext child = new GenericApplicationContext(parent);

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(child);
        reader.loadBeanDefinitions(String.valueOf((getClass().getResource("/childContext.xml"))));

        child.refresh();

        Printer printer = child.getBean("printer", Printer.class);
        assertThat(printer,is(notNullValue()));

        Hello hello = child.getBean("hello",Hello.class);
        assertThat(hello,is(notNullValue()));

        hello.print();
        assertThat(printer.toString(),is("Hello Child"));
    }

    @Test
    public void simpleBeanScanning()
    {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AnnotatedHelloConfig.class);
        AnnotatedHello hello = ctx.getBean("annotatedHello",AnnotatedHello.class);
        AnnotatedHelloConfig config = ctx.getBean("annotatedHelloConfig",AnnotatedHelloConfig.class);

        assertThat(hello, is(notNullValue()));
        assertThat(config, is(notNullValue()));

        //assertThat(config.annotatedHello(),is(not(sameInstance(hello))));
        //빈에 등록되어 사용되는 클래스는 디폴트로 싱글톤을 가진다. 그래서 이전에 호출한 오브젝트와 같은 오브젝트다.
        assertThat(config.annotatedHello(),is(sameInstance(hello)));
    }
}
