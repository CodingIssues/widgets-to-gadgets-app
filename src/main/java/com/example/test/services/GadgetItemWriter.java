package com.example.test.services;


import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.test.models.Gadget;
import com.example.test.repositories.GadgetRepository;

import java.util.List;


public class GadgetItemWriter extends RepositoryItemWriter<Gadget> {

    @Autowired
    public GadgetRepository gadgetRepository;


    private StepExecution stepExecution;

    public GadgetItemWriter(){
        this.setRepository(gadgetRepository);
        this.setMethodName("save");
    }


    @Override
    public void write(List<? extends Gadget> items) {
        ExecutionContext stepContext = this.stepExecution.getExecutionContext();
        int count = stepContext.containsKey("recordsWritten") ? stepContext.getInt("recordsWritten") : 0;
        stepContext.put("recordsWritten", count + items.size());
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

}