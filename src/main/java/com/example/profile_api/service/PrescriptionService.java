package com.example.profile_api.service;

import com.example.profile_api.dto.PrescriptionDTO;
import com.example.profile_api.model.Prescription;
import com.example.profile_api.model.Record;
import com.example.profile_api.repository.PrescriptionRepository;
import com.example.profile_api.repository.RecordRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private RecordRepository recordRepository;

    public PrescriptionDTO createPrescription(Prescription prescription) {
        // ... (logic xử lý) ...

        Prescription savedPrescription = prescriptionRepository.save(prescription);

        // Chuyển đổi Prescription sang PrescriptionDTO
        return new PrescriptionDTO(
                savedPrescription.getPrescriptionID(),
                savedPrescription.getVeterian().getVetID(),
                savedPrescription.getRecord().getRecordID(),
                savedPrescription.getMedication(),
                savedPrescription.getInstruction()
        );
    }

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public Optional<Prescription> getPrescriptionById(Integer id) {
        return prescriptionRepository.findById(id);
    }

    public Prescription updatePrescription(Integer id, Prescription prescriptionDetails) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuốc với ID: " + id));

        prescription.setVeterian(prescriptionDetails.getVeterian());
        prescription.setRecord(prescriptionDetails.getRecord());
        prescription.setMedication(prescriptionDetails.getMedication());
        prescription.setInstruction(prescriptionDetails.getInstruction());

        return prescriptionRepository.save(prescription);
    }

    public void deletePrescription(Integer id) {
        prescriptionRepository.deleteById(id);
    }
}
