package com.example.test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.example.test.models.Gadget;

@Repository
public interface GadgetRepository extends JpaRepository<Gadget, String> {

}
