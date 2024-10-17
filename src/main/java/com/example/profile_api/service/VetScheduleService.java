package com.example.profile_api.service;

import com.example.profile_api.model.VetSchedule;

import java.util.List;

public interface VetScheduleService {
    VetSchedule createVetSchedule(VetSchedule vetSchedule);
    List<VetSchedule> getAllVetSchedules();
    VetSchedule getVetScheduleById(Integer scheduleID);
    VetSchedule updateVetSchedule(Integer scheduleID, VetSchedule vetSchedule);
    void deleteVetSchedule(Integer scheduleID);
}