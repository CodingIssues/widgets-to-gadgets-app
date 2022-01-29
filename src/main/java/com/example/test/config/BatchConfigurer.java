package com.example.test.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;



@Component
public class BatchConfigurer extends DefaultBatchConfigurer {


    @Autowired
    @Qualifier("batchDataSource")
    private DataSource batchDataSource;


    @Bean
    @Override
    protected JobExplorer createJobExplorer() throws Exception
    {
        final JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
        jobExplorerFactoryBean.setDataSource(batchDataSource);
        jobExplorerFactoryBean.afterPropertiesSet();
        return jobExplorerFactoryBean.getObject();
    }


    public PlatformTransactionManager getTransactionManager() {
        final JdbcTransactionManager transactionManager = new JdbcTransactionManager();
        transactionManager.setDataSource(batchDataSource);
        return transactionManager;
    }
  

    @Bean
    @Override
    protected JobRepository createJobRepository() throws Exception {
        JobRepositoryFactoryBean fb = new JobRepositoryFactoryBean();
        fb.setDatabaseType("HSQL");
        fb.setDataSource(batchDataSource);
        fb.setTransactionManager(getTransactionManager());
        return fb.getObject();
    }


    @Bean
    @Override
    protected JobLauncher createJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(this.getJobRepository());
        jobLauncher.setTaskExecutor(new SyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }



}
