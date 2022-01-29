package com.example.test.repositories;

import com.example.test.models.Widget;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WidgetMongoRepository extends MongoRepository<Widget, String> {
}
