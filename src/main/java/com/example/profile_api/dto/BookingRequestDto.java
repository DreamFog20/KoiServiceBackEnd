package com.example.profile_api.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDto {
    private Integer userID;
    private Integer serviceID;
    private Integer paymentID;
    private Date date;
    // Các thuộc tính khác cần thiết
}