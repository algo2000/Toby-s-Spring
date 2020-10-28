package springbook.learningtest.spring.ioc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbook.learningtest.spring.ioc.bean.Hello;
import springbook.learningtest.spring.ioc.bean.Printer;
import springbook.learningtest.spring.ioc.bean.StringPrinter;

@Configuration
public class HelloConfig
{
    @Bean
    public Hello hello()
    {
        Hello hello = new Hello();
        hello.setName("Spring");
        hello.setPrinter(printer());    //DI를 위해 printer() 메소드를 여러 번 호출해도 매번 동일한 결과
        return hello;
    }

    @Bean
    public Hello hello2()
    {
        Hello hello = new Hello();
        hello.setName("Spring2");
        hello.setPrinter(printer());    //DI를 위해 printer() 메소드를 여러 번 호출해도 매번 동일한 결과
        return hello;
    }

    @Bean
    public Printer printer()
    {
        return new StringPrinter(); //이 메소드로 정의되는 빈은 싱글톤
    }
}
