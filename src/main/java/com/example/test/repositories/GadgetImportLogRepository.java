package com.example.test.repositories;

import com.example.test.models.GadgetImportResult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;


@Repository
public interface GadgetImportLogRepository extends JpaRepository<GadgetImportResult, Integer> {

    @Procedure
    void GadgetMergeUpsert();

    GadgetImportResult findFirstByOrderByGadgetImportJobIdDesc();
}
