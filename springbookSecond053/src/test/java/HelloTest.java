import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class HelloTest
{
    @Test
    public void registerBeanWithDependency()
    {
        /*
        <bean id = "printer" class = "StringPrinter"/>

        <bean id = "hello" class = "Hello">
            <property name = "name" value = "Spring"/>
            <property name = "printer" value = "printer"/>
        </bean>
         */
        StaticApplicationContext ac = new StaticApplicationContext();
        ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));

        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name","Spring");
        helloDef.getPropertyValues().addPropertyValue("printer",new RuntimeBeanReference("printer"));

        ac.registerBeanDefinition("hello",helloDef);

        Hello hello = ac.getBean("hello",Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString(),is("Hello Spring"));
    }
}
