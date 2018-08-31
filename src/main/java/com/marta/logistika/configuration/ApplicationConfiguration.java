package com.marta.logistika.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.marta.logistika")
@EnableJpaRepositories("com.marta.logistika.repository")
@Import(DataSourceConfiguration.class)
public class ApplicationConfiguration {

}
