package springbook.learningtest.jdk.proxy;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import springbook.learningtest.proxy.UppercaseHandler;

import java.lang.reflect.Proxy;

import static org.hamcrest.CoreMatchers.is;;
import static org.junit.Assert.assertThat;

public class DynamicProxyTest
{
    @Test
    public void simpleProxy()
    {
        Hello proxiedHello = (Hello)Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] {Hello.class},
                new UppercaseHandler(new HelloTarget())
        );
    }

    @Test
    public void classNamePointcutAdvisor()
    {
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut()
        {
            public ClassFilter getClassFilter()
            {
                return clazz -> clazz.getSimpleName().startsWith("HelloT");
            }
        };
        classMethodPointcut.setMappedName("sayH*");

        //테스트
        checkAdviced(new HelloTarget(), classMethodPointcut, true);

        class HelloWorld extends HelloTarget{};
        checkAdviced(new HelloWorld(), classMethodPointcut, false);

        class HelloToby extends HelloTarget{};
        checkAdviced(new HelloToby(), classMethodPointcut, true);


    }
    private  void checkAdviced(Object target, Pointcut pointcut, boolean adviced)
    {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
        Hello proxiedHello = (Hello)pfBean.getObject();

        if (adviced)
        {
            assertThat(proxiedHello.sayHello("Toby"),is("HELLO TOBY"));
            assertThat(proxiedHello.sayHi("Toby"),is("HI TOBY"));
            assertThat(proxiedHello.sayThankYou("Toby"), is("Thank You Toby"));
        }
        else
        {
            assertThat(proxiedHello.sayHello("Toby"),is("Hello Toby"));
            assertThat(proxiedHello.sayHi("Toby"),is("Hi Toby"));
            assertThat(proxiedHello.sayThankYou("Toby"), is("Thank You Toby"));
        }
    }

    @Test
    public void pointcutAdvisor()
    {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello)pfBean.getObject();

        assertThat(proxiedHello.sayHello("Toby"),is("HELLO TOBY"));
        assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
        assertThat(proxiedHello.sayThankYou("Toby"), is("Thank You Toby"));
    }

    @Test
    public void proxyFactoryBean()
    {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello)pfBean.getObject();

        assertThat(proxiedHello.sayHello("Toby"),is("HELLO TOBY"));
        assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
        assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
    }

    static class UppercaseAdvice implements MethodInterceptor
    {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable
        {
            String ret = (String)invocation.proceed();
            return ret.toUpperCase();
        }
    }

    static  interface  Hello
    {
        String sayHello(String name);
        String sayHi(String name);
        String sayThankYou(String name);
    }

    static class HelloTarget implements Hello
    {
        @Override
        public String sayHello(String name)
        {
            return "Hello " + name;
        }

        @Override
        public String sayHi(String name)
        {
            return "Hi "+name;
        }

        @Override
        public String sayThankYou(String name)
        {
            return "Thank You " + name;
        }
    }
}
