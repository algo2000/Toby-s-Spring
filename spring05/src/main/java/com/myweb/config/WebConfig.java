package com.myweb.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer
{

    // DispatcherServlet 설정
    @Override
    protected String[] getServletMappings()
    {
        return new String[] {"/"};
    }

    // Web Context Config 설정
    @Override
    protected Class<?>[] getServletConfigClasses()
    {
        return new Class[] {WebContext.class};
    }

    // Root Context Config 설정
    @Override
    protected Class<?>[] getRootConfigClasses()
    {
        return new Class[] {RootContext.class};
    }

    // Filter 설정
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        return new Filter[] {encodingFilter};
    }
}