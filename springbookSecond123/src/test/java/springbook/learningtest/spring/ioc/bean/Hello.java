package springbook.learningtest.spring.ioc.bean;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Map;

public class Hello
{
    private String name;

    @Autowired
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

    //@Autowired 부여 가능
    //@Autowired는 Resource와는 다르게 생성자에도 부여 가능하다. 그때는 생성자의 모든 파라미터에 타입에 의한 자동 와이어링이 적용
    public void setPrinter(Printer printer)
    {
        this.printer = printer;
    }

    //여러개의 빈이 탐색된 경우 모두 담을 수 있다.
//    @Autowired
//    Collection<Printer> printers;
//    //Set<Printer>, List<Printer>로 선언 가능
//
//    @Autowired
//    Printer[] printers;
//
//    @Autowired
//    Map<String,Printer> printerMap;
//    //String 빈 아이디가 키
//    //Printer타입의 빈 오브젝트가 값
}
