package com.myweb.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
@ComponentScan("com.myweb")
public class WebContext implements WebMvcConfigurer
{
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry)
    {
        WebMvcConfigurer.super.configureViewResolvers(registry);
        //View Resolver 설정
        registry.jsp("/WEB-INF/view/",".jsp");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        // Static 경로 설정
        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler("/**").addResourceLocations("/static/");
    }
}
