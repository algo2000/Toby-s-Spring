package springbook.learningtest.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler
{
    Object target;

    public UppercaseHandler(Object target)
    {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        Object ret = (String)method.invoke(target,args); //-> 타깃으로 기능 위임, 인터페이스의 메소드 호출에 모두 적용
        if (ret instanceof String && method.getName().startsWith("say"))  //리턴 타입이 string이면서 메소드 이름의 시작이 say인 경우에만 upper
        {
            return ((String)ret).toUpperCase(); //-> 부가기능 제공
        }
        else
        {
            return ret;
        }
    }
}
