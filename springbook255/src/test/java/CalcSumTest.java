import org.junit.Before;
import org.junit.Test;
import springbook.learningtest.template.Calculator;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CalcSumTest
{
    Calculator calculator;
    String numFilepath;

    @Before
    public void setUp() throws URISyntaxException
    {
        this.calculator = new Calculator();
        this.numFilepath = getClass().getResource("/numbers.txt").toURI().getPath();
    }

    @Test
    public void sumOfNumbers() throws IOException, URISyntaxException
    {
        assertThat(calculator.calcSum(this.numFilepath),is(10));
    }
    @Test
    public void multiplyOfNumbers() throws IOException
    {
        assertThat(calculator.calcMultiply(this.numFilepath),is(24));
    }
}
