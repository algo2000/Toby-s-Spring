package springbook.learningtest.proxy;

public class HelloUppercase implements Hello
{
    Hello hello; // 위임할 타깃 오브젝트 (즉, 현재시점에서의 HelloTarget이 들어감)

    public HelloUppercase(Hello hello)
    {
        this.hello = hello;
    }

    @Override
    public String sayHello(String name)
    {
        return hello.sayHello(name).toUpperCase(); //기능의 위임과 대문자화라는 부가 기능 적용
    }

    @Override
    public String sayHi(String name)
    {
        return hello.sayHi(name).toUpperCase();
    }

    @Override
    public String sayThankYou(String name)
    {
        return hello.sayThankYou(name).toUpperCase();
    }
}
