package com.example.profile_api.dao;

import com.example.profile_api.model.Veterian;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VetScheduleRequest {
    private Veterian veterinarian;
    private LocalDate scheduleDate;
    private String timeSlot;
    private String type;
    private Boolean availability;
}
