package com.example.test.models;


import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.OffsetDateTime;

@Document("Widgets")
public class Widget {
    @Id
    public String id;

    public OffsetDateTime creationDate;
    public OffsetDateTime lastUpdatedDate;
    public String testContent;

    public Widget() {

    }

}
