package com.myweb.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:config/db.properties")
public class ContextDataSource
{
    @Value("#{${db}}")
    private Map<String,String> db;

    @Bean
    public DataSource dataSource()
    {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(db.get("driver"));
        dataSource.setUrl(db.get("url"));
        dataSource.setUsername(db.get("username"));
        dataSource.setPassword(db.get("password"));
        dataSource.setDefaultAutoCommit(false);
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager()
    {
        return new DataSourceTransactionManager(dataSource());
    }
}
