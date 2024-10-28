
package com.example.profile_api.service;

import com.example.profile_api.dto.VetScheduleCreateDTO;
import com.example.profile_api.model.VetSchedule;
import com.example.profile_api.repository.VetScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

@Service
public class VetScheduleServiceImpl implements VetScheduleService {

    @Autowired
    private VetScheduleRepository vetScheduleRepository;

    @Override
    public VetSchedule createVetSchedule(VetSchedule vetSchedule) {
        return vetScheduleRepository.save(vetSchedule);
    }

    @Override
    public List<VetSchedule> getAllVetSchedules() {
        return vetScheduleRepository.findAll();
    }

    @Override
    public VetSchedule getVetScheduleById(Integer scheduleID) {
        return vetScheduleRepository.findById(scheduleID)
                .orElseThrow(() -> new RuntimeException("VetSchedule not found with ID: " + scheduleID));
    }

    @Override


    public VetSchedule updateVetSchedule(Integer scheduleID, VetScheduleCreateDTO vetScheduleCreateDTO) {
        VetSchedule existingVetSchedule = getVetScheduleById(scheduleID);

        existingVetSchedule.setScheduleDate(vetScheduleCreateDTO.getScheduleDate());


        String startTimeString = vetScheduleCreateDTO.getStartTime();
        String endTimeString = vetScheduleCreateDTO.getEndTime();

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("HH:mm"))
                .appendOptional(DateTimeFormatter.ofPattern("H:mm"))
                .toFormatter();
        LocalTime startTime = LocalTime.parse(startTimeString, formatter);
        LocalTime endTime = LocalTime.parse(endTimeString, formatter);

        existingVetSchedule.setStartTime(startTime);
        existingVetSchedule.setEndTime(endTime);

        existingVetSchedule.setType(vetScheduleCreateDTO.getType());
        existingVetSchedule.setAvailability(vetScheduleCreateDTO.getAvailability());

        return vetScheduleRepository.save(existingVetSchedule);
    }

    @Override
    public void deleteVetSchedule(Integer scheduleID) {
        vetScheduleRepository.deleteById(scheduleID);
    }
}
