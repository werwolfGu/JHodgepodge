package com.guce.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource(value = "classpath:jdbc-${spring.profiles.active}.properties")
public class DbSourceConfig {

    @Value("${jdbc.driverClassName}")
    private String jdbcDriverClassName;

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    @Value("${jdbc.initialSize:1}")
    private int initialSize;

    @Value("${jdbc.minIdle:1}")
    private int minIdle;

    @Value("${jdbc.maxActive:10}")
    private int maxActive;

    @Value("${jdbc.maxWait:10000}")
    private int maxWait;

    @Value("${jdbc.timeBetweenEvictionRunsMillis:60000}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${jdbc.minEvictableIdleTimeMillis:300000}")
    private int minEvictableIdleTimeMillis;

    @Value("${jdbc.testWhileIdle:true}")
    private boolean testWhileIdle;

    @Value("${jdbc.testOnBorrow:true}")
    private boolean testOnBorrow;
    @Value("${jdbc.testOnReturn:false}")
    private boolean testOnReturn;
    @Value("${jdbc.poolPreparedStatements:true}")
    private boolean poolPreparedStatements;

    @Value("${jdbc.maxPoolPreparedStatementPerConnectionSize:20}")
    private int maxPoolPreparedStatementPerConnectionSize;

    private String validationQuery = "select 1";

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

    @Bean("druidDataSource")
    @Scope("prototype")
    public DataSource druidDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setAsyncInit(true);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setDriverClassName(jdbcDriverClassName);
        druidDataSource.setUrl(jdbcUrl);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMinIdle(minIdle);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setMaxWait(maxWait);
        druidDataSource.setTimeBetweenConnectErrorMillis(timeBetweenEvictionRunsMillis);
        druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        druidDataSource.setTestOnBorrow(testOnBorrow);
        druidDataSource.setTestOnReturn(testOnReturn);
        druidDataSource.setTestWhileIdle(testWhileIdle);
        druidDataSource.setPoolPreparedStatements(poolPreparedStatements);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        druidDataSource.setValidationQuery(validationQuery);
        return druidDataSource;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception{
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(druidDataSource());
        Resource resource = new ClassPathResource("mybatis-config.xml");
        factory.setConfigLocation(resource);
        return factory.getObject();
    }

    /*@Bean("sqlSession")
    public SqlSession sqlSession() throws Exception {

        SqlSession sqlSession = new SqlSessionTemplate(sqlSessionFactory());
        return sqlSession;

    }*/

}
