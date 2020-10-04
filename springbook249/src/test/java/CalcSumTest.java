import org.junit.Test;
import springbook.learningtest.template.Calculator;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CalcSumTest
{
    @Test
    public void sumOfNumbers() throws IOException, URISyntaxException
    {
        Calculator calculator = new Calculator();
        int sum = calculator.calcSum(getClass().getResource("/numbers.txt").toURI().getPath());
        assertThat(sum,is(10));
    }
}
