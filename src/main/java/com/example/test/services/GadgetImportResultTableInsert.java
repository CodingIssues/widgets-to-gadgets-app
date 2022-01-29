package com.example.test.services;


import com.example.test.models.GadgetImportResult;
import com.example.test.repositories.GadgetImportLogRepository;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class GadgetImportResultTableInsert implements Tasklet {


    private GadgetImportLogRepository GadgetImportLogRepository;

    public GadgetImportResultTableInsert( GadgetImportLogRepository GadgetImportLogRepository ) {
        this.GadgetImportLogRepository = GadgetImportLogRepository;
    }


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        System.out.println("Uploading data from job");

        // Save the result from the run into the Export Tracker
        GadgetImportResult result = new GadgetImportResult();

        result.setOperationStartDateTime(OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime());

        result.setLastModifiedStartDate(chunkContext.getStepContext().getStepExecution()
                .getJobParameters().getDate("lastModifiedStartDate")
                .toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime());
        result.setLastModifiedEndDate(chunkContext.getStepContext().getStepExecution()
                .getJobParameters().getDate("lastModifiedEndDate")
                .toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime());

        Integer value = (Integer) chunkContext
                .getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .get("recordsWritten");

        if( value == null)
        {
            value = 0;
        }
        
        result.setTotalRecords(value);

        //
        // Insert into the MergeResult table
        GadgetImportLogRepository.saveAndFlush(result);

        System.out.println("Saved new result");

        return RepeatStatus.FINISHED;
    }
}