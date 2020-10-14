package springbook.learningtest.proxy;

import org.junit.Test;
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
}
