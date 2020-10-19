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
}
