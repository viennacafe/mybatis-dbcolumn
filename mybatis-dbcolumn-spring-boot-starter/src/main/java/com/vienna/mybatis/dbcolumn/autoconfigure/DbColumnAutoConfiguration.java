package com.vienna.mybatis.dbcolumn.autoconfigure;

import com.vienna.mybatis.dbcolumn.wrapper.DbColumnObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * {@code mybatis-dbcolumn-core}의 {@link DbColumnObjectWrapperFactory}를
 * MyBatis {@link Configuration}에 자동으로 등록하는 Spring Boot 자동설정입니다.
 *
 * MyBatis가 클래스패스에 있을 때만 활성화되며, 이 스타터 의존성만 추가하면
 * 애플리케이션에서 별도의 {@code @Configuration} 클래스 없이
 * {@code @DbColumn} 매핑이 바로 동작합니다.
 */
@AutoConfiguration
@ConditionalOnClass(Configuration.class)
public class DbColumnAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
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
