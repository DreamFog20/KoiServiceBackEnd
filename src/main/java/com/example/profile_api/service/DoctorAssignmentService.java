package com.example.profile_api.service;


import com.example.profile_api.model.VetSchedule;

import com.example.profile_api.model.Veterian;
import com.example.profile_api.repository.VetScheduleRepository;
import com.example.profile_api.repository.VeterianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class DoctorAssignmentService {

    @Autowired
    private VeterianRepository veterianRepository;

    @Autowired
    private VetScheduleRepository vetScheduleRepository;

    public Veterian assignDoctor(String serviceType, LocalDate date, LocalTime time) {
        // Tìm bác sĩ có chuyên môn phù hợp
        List<Veterian> vets = veterianRepository.findBySpecialization(serviceType);
        if (vets.isEmpty()) {
            return null; // Không tìm thấy bác sĩ có chuyên môn phù hợp
        }

        // Kiểm tra lịch trình của từng bác sĩ
        for (Veterian vet : vets) {
            Integer doctorId = vet.getVetID();
            List<VetSchedule> schedules = vetScheduleRepository.findByVeterianVetIDAndScheduleDate(doctorId, date);
            for (VetSchedule schedule : schedules) {
                if (schedule.getStartTime().isBefore(time) && schedule.getEndTime().isAfter(time) && schedule.getAvailability()) {
                    return vet; // Trả về bác sĩ đầu tiên có sẵn lịch trình
                }
            }
        }

        return null; // Không tìm thấy bác sĩ có sẵn lịch trình
    }
}