package com.example.profile_api.repository;

import com.example.profile_api.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface RecordRepository extends JpaRepository<Record, Integer> {
}
