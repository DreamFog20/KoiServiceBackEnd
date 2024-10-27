package com.example.profile_api.dto;



import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class VetScheduleCreateDTO {
    private LocalDate scheduleDate;
    private LocalDate startTime;
    private LocalDate endTime;
    private String type;
    private Boolean availability;
    private Integer vetID;
}