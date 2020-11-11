package com.myweb.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@MapperScan(basePackages = {"com.myweb.dao"})
public class ContextSqlMapper
{
    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws IOException
    {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:/mybatis/mybatis-config.xml"));
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mappers/*.xml"));
//        factoryBean.setConfigLocation(applicationContext.getResource("classpath:/mybatis/mybatis-config.xml"));
//        factoryBean.setMapperLocations(applicationContext.getResource("classpath:mybatis/mappers/Tag_SQL.xml"));
        return factoryBean;
    }

    @Bean
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory)
    {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
