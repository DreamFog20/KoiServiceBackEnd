package com.example.profile_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingCreateDTO {
    private String status;
    private Integer userId;  // Instead of a User object
    private Long vetId;   // Instead of a Vet object
    private Integer serviceId;  // Instead of a Service object
    private String date;
}