
package com.example.profile_api.dao;

import com.example.profile_api.model.Veterian;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class VetScheduleRequest {
    private Veterian veterinarian;
    private LocalDate scheduleDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String type;
    private Boolean availability;
}
