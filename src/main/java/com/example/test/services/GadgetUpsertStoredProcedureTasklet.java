package com.example.test.services;


import com.example.test.repositories.GadgetImportLogRepository;
import org.springframework.batch.core.StepContribution;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;



public class GadgetUpsertStoredProcedureTasklet implements Tasklet {

    private GadgetImportLogRepository GadgetImportLogRepository;

    public GadgetUpsertStoredProcedureTasklet( GadgetImportLogRepository GadgetImportLogRepository ) {
        this.GadgetImportLogRepository = GadgetImportLogRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        GadgetImportLogRepository.GadgetMergeUpsert();
        return RepeatStatus.FINISHED;
    }
}