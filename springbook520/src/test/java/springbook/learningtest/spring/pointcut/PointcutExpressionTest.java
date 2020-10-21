package springbook.learningtest.spring.pointcut;

import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class PointcutExpressionTest
{
    @Test
    public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException
    {
        System.out.println(Target.class.getMethod("minus",int.class,int.class));

        AspectJExpressionPointcut poincut = new AspectJExpressionPointcut();
        poincut.setExpression("execution(public int springbook.learningtest.spring.pointcut.Target.minus(int,int) throws java.lang.RuntimeException)");

        assertThat(poincut.getClassFilter().matches(Target.class) &&
                poincut.getMethodMatcher().matches(
                        Target.class.getMethod("minus",int.class,int.class),null),is(true));

        assertThat(poincut.getClassFilter().matches(Target.class) &&
                poincut.getMethodMatcher().matches(
                        Target.class.getMethod("plus",int.class,int.class),null),is(false));

        assertThat(poincut.getClassFilter().matches(Bean.class) &&
                poincut.getMethodMatcher().matches(
                        Target.class.getMethod("method"),null),is(false));
    }

    @Test
    public void pointcut() throws Exception
    {
        targetClassPointCutMathes("execution(* *(..))",true,true,true,true,true,true);
    }

    public void targetClassPointCutMathes(String expression, boolean... expected) throws NoSuchMethodException
    {
        pointcutMatches(expression,expected[0],Target.class,"hello");
        pointcutMatches(expression,expected[1],Target.class,"hello",String.class);
        pointcutMatches(expression,expected[2],Target.class,"plus",int.class, int.class);
        pointcutMatches(expression,expected[3],Target.class,"minus",int.class,int.class);
        pointcutMatches(expression,expected[4],Target.class,"method");
        pointcutMatches(expression,expected[5],Bean.class,"method");
    }

    public void pointcutMatches(String expression,Boolean expected, Class<?> clazz, String methodName, Class<?>... args) throws NoSuchMethodException
    {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);

        assertThat(pointcut.getClassFilter().matches(clazz)
                && pointcut.getMethodMatcher().matches(clazz.getMethod(methodName,args),null),
                is(expected));
    }
}
