package com.example.demo.primary.config;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.demo.primary.repository",
        entityManagerFactoryRef = "primaryEntityManagerFactory",
        transactionManagerRef = "primaryTransactionManager"
)
public class PrimaryDBConfig {

    // ----------------------------
    // Primary DataSource Properties
    // ----------------------------
    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource")
    public org.springframework.boot.jdbc.autoconfigure.DataSourceProperties primaryDataSourceProperties() {
        return new org.springframework.boot.jdbc.autoconfigure.DataSourceProperties();
    }

    // ----------------------------
    // Primary DataSource
    // ----------------------------
    @Primary
    @Bean
    public DataSource primaryDataSource() {
        return primaryDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    // ----------------------------
    // Primary Entity Manager
    // ----------------------------
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            org.springframework.boot.jpa.EntityManagerFactoryBuilder builder) {

        return builder
                .dataSource(primaryDataSource())
                .packages("com.example.demo.entity")
                .persistenceUnit("primary")
                .build();
    }

    // ----------------------------
    // Primary Transaction Manager
    // ----------------------------
    @Primary
    @Bean
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryEntityManagerFactory")
            EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }
}
