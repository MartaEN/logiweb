package com.marta.logistika.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.marta.logistika")
@Import(value = {
        DataSourceConfig.class,
} )
public class ApplicationConfig {


}
