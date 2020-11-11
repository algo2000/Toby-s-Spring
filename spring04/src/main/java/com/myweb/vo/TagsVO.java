package com.myweb.vo;

public class TagsVO
{
    //태그 항목 이름 예시) artist, tag
    private String name;
    //태그 값) female, anal
    private  String value;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return name+":"+value;
    }
}
