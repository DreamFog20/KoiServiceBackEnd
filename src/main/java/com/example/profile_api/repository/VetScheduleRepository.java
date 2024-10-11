package com.example.profile_api.repository;

import com.example.profile_api.model.VetSchedule;
import com.example.profile_api.model.Veterian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface VetScheduleRepository extends JpaRepository<VetSchedule, Integer> {

    // Tìm kiếm lịch làm việc của bác sĩ theo bác sĩ, ngày, giờ và trạng thái rảnh/bận
    List<VetSchedule> findByVeterianAndScheduleDateAndTimeSlotAndAvailability(
            Veterian veterian, LocalDate scheduleDate, LocalTime timeSlot, Boolean availability);

}
