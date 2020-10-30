package springbook.learningtest.spring.ioc.annotation;

import org.springframework.stereotype.Component;
import springbook.learningtest.spring.ioc.bean.Printer;

@Component
public class StringPrinter implements Printer
{
    private  StringBuffer buffer = new StringBuffer();

    @Override
    public void print(String message)
    {
        this.buffer.append(message);
    }

    public String toString()
    {
        return this.buffer.toString();
    }
}
