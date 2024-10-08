package com.example.profile_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "VetSchedule")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VetSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ScheduleID")
    private Integer scheduleID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VetID", nullable = false)
    private Veterian veterian; // Quan hệ với bảng Veterian (Bác sĩ thú y)

    @Column(name = "Schedule_Date", nullable = false)
    private LocalDate scheduleDate; // Ngày làm việc của bác sĩ

    @Column(name = "Time_Slot", nullable = false)
    private LocalTime timeSlot; // Khoảng thời gian làm việc (có thể là giờ)

    @Column(name = "Type", nullable = false)
    private String type; // Loại lịch (có thể là kiểm tra, tư vấn, ...)

    @Column(name = "Availability", nullable = false)
    private Boolean availability; // Tình trạng sẵn có của bác sĩ (true: sẵn sàng, false: không sẵn sàng)
}

