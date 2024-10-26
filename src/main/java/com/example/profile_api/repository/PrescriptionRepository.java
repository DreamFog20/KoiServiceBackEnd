package com.example.profile_api.repository;

import com.example.profile_api.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
        }