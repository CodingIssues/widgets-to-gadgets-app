package com.example.test.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;


@Configuration
@EnableBatchProcessing
public class DatabaseConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.first-datasource")
    public DataSource gadgetDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @BatchDataSource
    @ConfigurationProperties(prefix="spring.second-datasource")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create().build();
    }

}





