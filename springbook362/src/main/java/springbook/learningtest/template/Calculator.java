package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator
{
    public Integer calcSum(String filepath) throws IOException
    {
        LineCallback<Integer> sumCallback = ((line,value) -> value + Integer.valueOf(line));
        return lineReadTemplate(filepath,sumCallback,0);
    }

    public Integer calcMultiply(String filepath) throws IOException
    {
        LineCallback<Integer> sumCallback = ((line,value) -> value * Integer.valueOf(line));
        return lineReadTemplate(filepath,sumCallback,1);
    }

    public String concatenate(String filepath) throws IOException
    {
        LineCallback<String> concatenateCallback = ((line,value) -> value + line);
        return lineReadTemplate(filepath,concatenateCallback,"");
    }

    public <T> T lineReadTemplate(String filepath,LineCallback<T> callback,T initVal) throws IOException
    {
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader(filepath));
            T res = initVal;
            String line = null;
            while((line = br.readLine())!=null)
            {
                res = callback.doSomethingWithLine(line,res);
            }
            return res;
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            throw e;
        }
        finally
        {
            if (br!=null){try{br.close();}catch (IOException e){System.out.println(e.getMessage());}}
        }
    }
}
