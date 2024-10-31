package com.example.profile_api.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingDTO {
    private Integer bookingID;
    private String status;
    private Integer userID;
    private String userName;
    private Integer vetID;
    private String vetName;
    private Date date;

}
