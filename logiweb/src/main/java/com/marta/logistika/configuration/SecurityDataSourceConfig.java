package com.marta.logistika.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:datasource_security.properties")
public class SecurityDataSourceConfig {

    private final Environment env;

    @Autowired
    public SecurityDataSourceConfig(Environment env) {
        this.env = env;
    }

    @Bean("securityDataSource")
    public DataSource dataSourceSecurity() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("datasource_security.driver"));
        dataSource.setUrl(env.getRequiredProperty("datasource_security.url"));
        dataSource.setUsername(env.getRequiredProperty("datasource_security.user"));
        dataSource.setPassword(env.getRequiredProperty("datasource_security.password"));
        return dataSource;
    }

}
