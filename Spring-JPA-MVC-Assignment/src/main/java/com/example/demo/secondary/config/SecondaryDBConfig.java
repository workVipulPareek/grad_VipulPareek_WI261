package com.example.demo.secondary.config;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.demo.secondary.repository",
        entityManagerFactoryRef = "secondaryEntityManagerFactory",
        transactionManagerRef = "secondaryTransactionManager"
)
public class SecondaryDBConfig {

    // ----------------------------
    // Secondary DataSource Properties
    // ----------------------------
    @Bean
    @ConfigurationProperties("spring.second-datasource")
    public org.springframework.boot.jdbc.autoconfigure.DataSourceProperties secondaryDataSourceProperties() {
        return new org.springframework.boot.jdbc.autoconfigure.DataSourceProperties();
    }

    // ----------------------------
    // Secondary DataSource
    // ----------------------------
    @Bean
    public DataSource secondaryDataSource() {
        return secondaryDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    // ----------------------------
    // Secondary Entity Manager
    // ----------------------------
    @Bean
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
            org.springframework.boot.jpa.EntityManagerFactoryBuilder builder) {

        return builder
                .dataSource(secondaryDataSource())
                .packages("com.example.demo.entity")
                .persistenceUnit("secondary")
                .build();
    }

    // ----------------------------
    // Secondary Transaction Manager
    // ----------------------------
    @Bean
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("secondaryEntityManagerFactory")
            EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }
}
