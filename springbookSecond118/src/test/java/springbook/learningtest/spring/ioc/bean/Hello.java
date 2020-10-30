package springbook.learningtest.spring.ioc.bean;

import javax.annotation.Resource;

public class Hello
{
    private String name;
    //@Resource(name="printer") 을 하면 Setter 메소드를 추가하지 않아도 된다.
    private Printer printer;

    public String sayHello()
    {
        return "Hello " + name;
    }

    public void print()
    {
        this.printer.print(sayHello());
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Resource(name="printer")
    public void setPrinter(Printer printer)
    {
        this.printer = printer;
    }
}
