package com.example.profile_api.controller;

import com.example.profile_api.model.Prescription;
import com.example.profile_api.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions") // Đường dẫn cho Prescription
@CrossOrigin(origins = "http://localhost:5173")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    // Tạo mới một đơn thuốc
    @PostMapping
    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescription) {
        Prescription createdPrescription = prescriptionService.createPrescription(prescription);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPrescription);
    }

    // Lấy danh sách tất cả đơn thuốc
    @GetMapping
    public List<Prescription> getAllPrescriptions() {
        return prescriptionService.getAllPrescriptions();
    }

    // Lấy thông tin đơn thuốc theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable("id") Integer id) {
        return prescriptionService.getPrescriptionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cập nhật thông tin đơn thuốc
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(
            @PathVariable("id") Integer id,
            @RequestBody Prescription prescriptionDetails) {
        Prescription updatedPrescription = prescriptionService.updatePrescription(id, prescriptionDetails);
        return ResponseEntity.ok(updatedPrescription);
    }

    // Xóa đơn thuốc theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable("id") Integer id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}
