package com.example.profile_api.dto;

import com.example.profile_api.model.User;
import com.example.profile_api.model.VetSchedule;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {
    private Integer bookingID;
    private String status;
    private User user;
    private Service service;
    private Date date;
    private VetSchedule vetSchedule;
}
