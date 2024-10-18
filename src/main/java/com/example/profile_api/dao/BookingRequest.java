
package com.example.profile_api.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    private String serviceType;
    private LocalTime startTime;
    private LocalTime endTime;
    private String timeSlot;
}
