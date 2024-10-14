
package com.example.profile_api.controller;

import com.example.profile_api.dao.VetScheduleRequest;
import com.example.profile_api.model.VetSchedule;
import com.example.profile_api.model.Veterian;
import com.example.profile_api.repository.VeterianRepository;
import com.example.profile_api.service.VetScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/vetschedules")
public class VetScheduleController {

    @Autowired
    private VetScheduleService vetScheduleService;
    @Autowired
    private VeterianRepository veterianRepository;

    @PostMapping

    public ResponseEntity<VetSchedule> createVetSchedule(@RequestBody VetScheduleRequest request) {
        // Tạo VetSchedule từ VetScheduleRequest
        VetSchedule vetSchedule = new VetSchedule();
        vetSchedule.setScheduleDate(request.getScheduleDate());

        //  Sử dụng startTime và endTime từ request
        vetSchedule.setStartTime(request.getStartTime());
        vetSchedule.setEndTime(request.getEndTime());

        vetSchedule.setType(request.getType());
        vetSchedule.setAvailability(request.getAvailability());

        // Lấy Veterinarian từ database dựa trên vetID trong request
        Veterian veterian = veterianRepository.findById(request.getVeterinarian().getVetID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veterian not found"));
        vetSchedule.setVeterian(veterian);

        VetSchedule createdVetSchedule = vetScheduleService.createVetSchedule(vetSchedule);
        return new ResponseEntity<>(createdVetSchedule, HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<List<VetSchedule>> getAllVetSchedules() {
        List<VetSchedule> vetSchedules = vetScheduleService.getAllVetSchedules();
        return new ResponseEntity<>(vetSchedules, HttpStatus.OK);
    }

    @GetMapping("/{scheduleID}")
    public ResponseEntity<VetSchedule> getVetScheduleById(@PathVariable Integer scheduleID) {
        VetSchedule vetSchedule = vetScheduleService.getVetScheduleById(scheduleID);
        return new ResponseEntity<>(vetSchedule, HttpStatus.OK);
    }

    @PutMapping("/{scheduleID}")
    public ResponseEntity<VetSchedule> updateVetSchedule(@PathVariable Integer scheduleID, @RequestBody VetSchedule vetSchedule) {
        VetSchedule updatedVetSchedule = vetScheduleService.updateVetSchedule(scheduleID, vetSchedule);
        return new ResponseEntity<>(updatedVetSchedule, HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleID}")
    public ResponseEntity<Void> deleteVetSchedule(@PathVariable Integer scheduleID) {
        vetScheduleService.deleteVetSchedule(scheduleID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
