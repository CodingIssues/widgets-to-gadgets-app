package com.example.test.config;

import java.util.Date;

import javax.sql.DataSource;

import com.example.test.models.Gadget;
import com.example.test.models.Widget;
import com.example.test.repositories.GadgetImportLogRepository;
import com.example.test.services.GadgetImportResultTableInsert;
import com.example.test.services.GadgetItemWriter;
import com.example.test.services.GadgetUpsertStoredProcedureTasklet;
import com.example.test.services.WidgetToGadgetItemProcessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@EnableBatchProcessing
public class GadgetImportConfig {

    public static final String JOB_NAME = "GadgetImportJob";


    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Autowired
    private MongoTemplate mongoTemplate;


    @Autowired
    @Qualifier("gadgetDataSource")
    private DataSource gadgetDataSource;

    @Autowired
    private GadgetImportLogRepository GadgetImportLogRepository;



    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(gadgetDataSource);
        return transactionManager;
    }


    @Bean
    @StepScope
    public MongoItemReader<Widget> reader(
            @Value("#{jobParameters[lastModifiedStartDate]}") Date lastModifiedStartDate,
            @Value("#{jobParameters[lastModifiedEndDate]}") Date lastModifiedEndDate
    ) {

        System.out.println("Start Date in reader: " + lastModifiedStartDate.toString());
        System.out.println("End Date in reader: " + lastModifiedEndDate.toString());


        MongoItemReader<Widget> reader = new MongoItemReader<>();
        reader.setTemplate(mongoTemplate);
        reader.setTargetType(Widget.class);

        Criteria criteria = new Criteria().orOperator(
                Criteria.where("lastUpdatedDate").ne(null).gte(lastModifiedStartDate).lte(lastModifiedEndDate),
                new Criteria().andOperator(
                        Criteria.where("lastUpdatedDate").is(null),
                        Criteria.where("creationDate").gte(lastModifiedStartDate).lte(lastModifiedEndDate)
                ));

        Query query = new Query();
        query.addCriteria(criteria);
        reader.setQuery(query);

        return reader;
    }

    public WidgetToGadgetItemProcessor processor() {
        return new WidgetToGadgetItemProcessor();
    }


    public GadgetItemWriter writer() {
        return new GadgetItemWriter();
    }




    public ExecutionContextPromotionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

        listener.setKeys(new String[] {"recordsWritten"});

        return listener;
    }


    @Bean
    public Job GadgetImportJob() throws Exception {
        return jobBuilderFactory.get("GadgetImportJob")
                .start(ImportGadgetsToStaging())
                .next(CallUpsertStoredProcedure())
                .next(LogResult())
                .build();
    }



    @Bean
    public Step ImportGadgetsToStaging() throws Exception {
        return stepBuilderFactory.get("ImportGadgetsToStaging")
                .transactionManager(platformTransactionManager())
                .<Widget, Gadget>chunk(2)
                .reader(reader(null, null))
                .processor(processor())
                .writer(writer())
                .listener(promotionListener())
                .build();
    }


    // Step 2 is calling the Upsert Stored Procedure
    @Bean
    public Step CallUpsertStoredProcedure() throws Exception {
        return stepBuilderFactory.get("CallUpsertStoredProcedure")
                .tasklet( new GadgetUpsertStoredProcedureTasklet(GadgetImportLogRepository))
                .build();
    }



    // Step 3 is writing out the status of the job to the database...
    @Bean
    public Step LogResult() throws Exception {
        return stepBuilderFactory.get("LogResult")
                .transactionManager(platformTransactionManager())
                .tasklet(new GadgetImportResultTableInsert(GadgetImportLogRepository))
                .build();
    }


}