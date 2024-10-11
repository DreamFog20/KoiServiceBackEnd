package com.example.profile_api.service;


import com.example.profile_api.model.Veterian;
import com.example.profile_api.repository.VeterianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeterianService {

    private final VeterianRepository veterianRepository;

    @Autowired
    public VeterianService(VeterianRepository veterianRepository) {
        this.veterianRepository = veterianRepository;
    }

    // 1. Tạo mới một Veterian
    public Veterian createVeterian(Veterian veterian) {
        return veterianRepository.save(veterian);
    }

    // 2. Lấy danh sách tất cả các Veterian
    public List<Veterian> getAllVeterians() {
        return veterianRepository.findAll();
    }

    // 3. Lấy thông tin Veterian theo ID
    public Optional<Veterian> getVeterianById(Integer vetID) {
        return veterianRepository.findById(vetID);
    }

    // 4. Cập nhật thông tin Veterian
    public Veterian updateVeterian(Integer vetID, Veterian veterianDetails) {
        Veterian veterian = veterianRepository.findById(vetID)
                .orElseThrow(() -> new RuntimeException("Veterian not found with id " + vetID));

        veterian.setName(veterianDetails.getName());
        veterian.setPhone(veterianDetails.getPhone());
        veterian.setEmail(veterianDetails.getEmail());
        veterian.setSpecialization(veterianDetails.getSpecialization());


        return veterianRepository.save(veterian);
    }

    // 5. Xóa một Veterian theo ID
    public void deleteVeterian(Integer vetID) {
        Veterian veterian = veterianRepository.findById(vetID)
                .orElseThrow(() -> new RuntimeException("Veterian not found with id " + vetID));
        veterianRepository.delete(veterian);
    }
}
