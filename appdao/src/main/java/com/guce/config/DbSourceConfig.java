package com.guce.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource(value = "classpath:jdbc-${spring.profiles.active}.properties")
public class DbSourceConfig {


    @Primary
    @Bean(destroyMethod = "")
    @ConfigurationProperties("app.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(BasicDataSource.class).build();
    }

    @Bean(destroyMethod = "")
    @ConfigurationProperties("second.datasource")
    public DataSource secondDataSource(){
        return DataSourceBuilder.create().type(BasicDataSource.class).build();
    }

    @Bean("jdbcTemplate")
    public JdbcTemplate jdbcTemplate(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }

    @Bean("secondJdbcTemplate")
    public JdbcTemplate secondJdbcTemplate(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(secondDataSource());
        return jdbcTemplate;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception{
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource());
        Resource resource = new ClassPathResource("mybatis-config.xml");
        factory.setConfigLocation(resource);
        return factory.getObject();
    }

    @Bean("sqlSession")
    public SqlSession sqlSession() throws Exception {

        SqlSession sqlSession = new SqlSessionTemplate(sqlSessionFactory());
        return sqlSession;

    }

}
