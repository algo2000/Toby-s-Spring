package springbook.learningtest.spring.ioc.bean;

import org.springframework.context.annotation.Bean;

public class HelloService
{
    private Printer printer;

    public void setPrinter(Printer printer)
    {
        this.printer = printer;
    }

    @Bean
    private Hello hello()
    {
        Hello hello = new Hello();
        hello.setName("Spring");
//        hello.setPrinter(printer());    //Configuration을 사용하지 않았기 때문에 싱글톤이 아님 그래서 오류
        hello.setPrinter(this.printer);
        return hello;
    }

    @Bean
    private Hello hello2()
    {
        Hello hello = new Hello();
        hello.setName("Spring2");
//        hello.setPrinter(printer());    //Configuration을 사용하지 않았기 때문에 싱글톤이 아님 그래서 오류
        hello.setPrinter(this.printer);
        return hello;
    }

    @Bean
    private Printer printer()
    {
        return new StringPrinter(); //이 메소드로 정의되는 빈은 싱글톤
    }
}
