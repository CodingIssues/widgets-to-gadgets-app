package com.example.test.models;


//import com.fasterxml.jackson.annotation.JsonProperty;
//import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDateTime;


@Entity(name = "GadgetImportResult")
@Table(name = "GadgetImportResult")
public class GadgetImportResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gadgetImportJobId")
    private Integer gadgetImportJobId;

    private LocalDateTime operationStartDateTime;

    private LocalDateTime operationEndDateTime;

    private LocalDateTime lastModifiedStartDate;

    private LocalDateTime lastModifiedEndDate;

    private int totalRecords;



    public Integer getGadgetImportJobId() {
        return gadgetImportJobId;
    }


    public LocalDateTime getOperationStartDateTime() {
        return operationStartDateTime;
    }

    public void setOperationStartDateTime(LocalDateTime operationDateTime) {
        this.operationStartDateTime = operationDateTime;
    }

    public LocalDateTime getOperationEndDateTime() {
        return operationEndDateTime;
    }

    public void setOperationEndDateTime(LocalDateTime operationDateTime) {
        this.operationEndDateTime = operationDateTime;
    }




    public LocalDateTime getLastModifiedStartDate() {
        return lastModifiedStartDate;
    }

    public void setLastModifiedStartDate(LocalDateTime lastModifiedStartDate) {
        this.lastModifiedStartDate = lastModifiedStartDate;
    }

    public LocalDateTime getLastModifiedEndDate() {
        return lastModifiedEndDate;
    }

    public void setLastModifiedEndDate(LocalDateTime lastModifiedEndDate) {
        this.lastModifiedEndDate = lastModifiedEndDate;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public GadgetImportResult(){ }

    public GadgetImportResult(LocalDateTime operationStartDateTime, LocalDateTime operationEndDateTime, LocalDateTime lastModifiedStartDate, LocalDateTime lastModifiedEndDate, int totalRecords ){
        this.operationStartDateTime = operationStartDateTime;
        this.operationEndDateTime = operationEndDateTime;
        this.lastModifiedStartDate = lastModifiedStartDate;
        this.lastModifiedEndDate = lastModifiedEndDate;
        this.totalRecords = totalRecords;
    }




}
