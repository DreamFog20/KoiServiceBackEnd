package com.example.profile_api.service;

import com.example.profile_api.model.VetSchedule;
import com.example.profile_api.repository.VetScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public VetSchedule updateVetSchedule(Integer scheduleID, VetSchedule vetSchedule) {
        VetSchedule existingVetSchedule = getVetScheduleById(scheduleID);
        existingVetSchedule.setVeterian(vetSchedule.getVeterian());
        existingVetSchedule.setScheduleDate(vetSchedule.getScheduleDate());

        // Cập nhật startTime và endTime thay vì timeSlot
        existingVetSchedule.setStartTime(vetSchedule.getStartTime());
        existingVetSchedule.setEndTime(vetSchedule.getEndTime());

        existingVetSchedule.setType(vetSchedule.getType());
        existingVetSchedule.setAvailability(vetSchedule.getAvailability());
        return vetScheduleRepository.save(existingVetSchedule);
    }

    @Override
    public void deleteVetSchedule(Integer scheduleID) {
        vetScheduleRepository.deleteById(scheduleID);
    }
}