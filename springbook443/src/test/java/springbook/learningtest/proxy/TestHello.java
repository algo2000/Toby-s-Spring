package springbook.learningtest.proxy;

import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestHello
{
    @Test
    public void simpleProxy()
    {
        Hello hello = new HelloTarget(); // -> 타겟은 인터페이스를 통해 접근하는 습관 들이기
        assertThat(hello.sayHello("Toby"),is("Hello Toby"));
        assertThat(hello.sayHi("Toby"),is("Hi Toby"));
        assertThat(hello.sayThankYou("Toby"),is("Thank You Toby"));
    }

    @Test
    public void simpleProxy2()
    {
        Hello hello = new HelloUppercase(new HelloTarget()); // -> 프록시를 통해 타깃 오브젝트에 접근하도록 구
        assertThat(hello.sayHello("Toby"),is("HELLO TOBY"));
        assertThat(hello.sayHi("Toby"),is("HI TOBY"));
        assertThat(hello.sayThankYou("Toby"),is("THANK YOU TOBY"));
    }

    @Test
    public void simpleProxy3()
    {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
            getClass().getClassLoader(), //-> 동적으로 생성되는 다이내믹 프록시 클래스의 로딩에 사용할 클래스 로
            new Class[] {Hello.class},  //-> 구현할 인터페이스
            new UppercaseHandler(new HelloTarget()) //->부가기능과 위임 코드를 담은 InvpocationHandler
        );
        assertThat(proxiedHello.sayHello("Toby"),is("HELLO TOBY"));
        assertThat(proxiedHello.sayHi("Toby"),is("HI TOBY"));
        assertThat(proxiedHello.sayThankYou("Toby"),is("THANK YOU TOBY"));
    }
}
