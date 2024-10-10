package com.example.profile_api.repository;

import com.example.profile_api.model.Veterian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeterianRepository extends JpaRepository<Veterian, Integer> {

}

