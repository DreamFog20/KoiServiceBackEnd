package com.example.profile_api.repository;

import com.example.profile_api.model.Veterian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface VeterianRepository extends JpaRepository<Veterian, Integer> {

    // Tìm kiếm bác sĩ theo chuyên môn
    List<Veterian> findBySpecialization(String specialization);


}

