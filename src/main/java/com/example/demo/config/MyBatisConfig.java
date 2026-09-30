package com.example.demo.config;

import com.example.demo.mybatis.wrapper.DbColumnObjectWrapperFactory;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {

    @Bean
    public DbColumnObjectWrapperFactory dbColumnObjectWrapperFactory() {
        return new DbColumnObjectWrapperFactory();
    }

    @Bean
    public ConfigurationCustomizer dbColumnConfigurationCustomizer(
            DbColumnObjectWrapperFactory factory
    ) {
        return configuration -> configuration.setObjectWrapperFactory(factory);
    }
}
