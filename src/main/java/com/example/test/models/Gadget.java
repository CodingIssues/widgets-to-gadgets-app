package com.example.test.models;


import javax.persistence.*;
import java.time.LocalDateTime;



@Entity(name = "Gadgets")
@Table(name = "Gadgets_Staging")
public class Gadget {

    @Id
    private String gadget_id;


    @Column(name = "creationDate")
    private LocalDateTime creationDate;

    private LocalDateTime lastModifiedDate;


    public Gadget() {
    }

    public String getGadget_id() {
        return gadget_id;
    }

    public void setGadget_id(String gadget_id) {
        this.gadget_id = gadget_id;
    }

    public LocalDateTime getCreationDate() { return creationDate; }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

}
