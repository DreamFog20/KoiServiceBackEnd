package com.example.profile_api.controller;

import com.example.profile_api.dto.VetScheduleCreateDTO;
import com.example.profile_api.model.VetSchedule;
import com.example.profile_api.model.Veterian;
import com.example.profile_api.repository.VeterianRepository;
import com.example.profile_api.service.VetScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/vetschedules")
public class VetScheduleController {

    @Autowired
    private VetScheduleService vetScheduleService;
    @Autowired
    private VeterianRepository veterianRepository;

    @PostMapping("/create")
    public ResponseEntity<VetSchedule> createVetSchedule(@RequestBody VetScheduleCreateDTO request) {
        VetSchedule vetSchedule = new VetSchedule();
        vetSchedule.setScheduleDate(request.getScheduleDate());

        // Lấy giá trị String từ request
        String startTimeString = request.getStartTime();
        String endTimeString = request.getEndTime();


        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("HH:mm"))
                .appendOptional(DateTimeFormatter.ofPattern("H:mm"))
                .toFormatter();
        LocalTime startTime = LocalTime.parse(startTimeString, formatter);
        LocalTime endTime = LocalTime.parse(endTimeString, formatter);

        // Gán giá trị LocalTime cho vetSchedule
        vetSchedule.setStartTime(startTime);
        vetSchedule.setEndTime(endTime);

        vetSchedule.setType(request.getType());
        vetSchedule.setAvailability(request.getAvailability());

        Veterian veterian = veterianRepository.findById(request.getVetID())
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
    public ResponseEntity<VetSchedule> updateVetSchedule(@PathVariable Integer scheduleID, @RequestBody VetScheduleCreateDTO vetScheduleCreateDTO) {
        VetSchedule updatedVetSchedule = vetScheduleService.updateVetSchedule(scheduleID, vetScheduleCreateDTO);
        return new ResponseEntity<>(updatedVetSchedule, HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleID}")
    public ResponseEntity<Void> deleteVetSchedule(@PathVariable Integer scheduleID) {
        vetScheduleService.deleteVetSchedule(scheduleID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}