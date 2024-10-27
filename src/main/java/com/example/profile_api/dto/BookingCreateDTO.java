    package com.example.profile_api.dto;

    import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
    import lombok.Getter;
    import lombok.Setter;

    import java.util.Date;

    @Getter
    @Setter
    public class BookingCreateDTO {
        private String status;
        private int userID;
        private int vetID;
        private int serviceID;
        private Date date;
        private int  scheduleID;
    }