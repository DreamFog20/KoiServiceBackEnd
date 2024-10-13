package com.example.profile_api.repository;


import com.example.profile_api.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByVetVetIDAndDate(Integer vetId, LocalDate date);

}