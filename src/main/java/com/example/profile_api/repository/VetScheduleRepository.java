
package com.example.profile_api.repository;

import com.example.profile_api.model.VetSchedule;
import com.example.profile_api.model.Veterian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository

public interface VetScheduleRepository extends JpaRepository<VetSchedule, Integer> {
    // Tìm kiếm lịch làm việc của bác sĩ

    List<VetSchedule> findByScheduleDateAndAvailability(LocalDate scheduleDate, Boolean availability);

    List<VetSchedule> findByVeterianVetIDAndScheduleDateAndAvailability(Integer vetId, LocalDate scheduleDate, Boolean availability);
    @Query("SELECT vs FROM VetSchedule vs WHERE vs.veterian.vetID = :vetId AND vs.scheduleDate = :scheduleDate")
    List<VetSchedule> findByVetIdAndScheduleDate(
            @Param("vetId") Integer vetId,
            @Param("scheduleDate") LocalDate scheduleDate);
}
