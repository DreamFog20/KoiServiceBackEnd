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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "VetID", nullable = true)
    private Veterian veterian;

    @Column(name = "Schedule_Date", nullable = false)
    private LocalDate scheduleDate;

    @Column(name = "startTime", nullable = false)
    private LocalTime startTime;

    @Column(name = "endTime", nullable = false)
    private LocalTime endTime;

    @Column(name= "Type", nullable = false)
    private String type;

    @Column(name = "Availability", nullable = false)
    private Boolean availability;
}