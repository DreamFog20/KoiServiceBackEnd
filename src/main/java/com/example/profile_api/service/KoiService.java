package com.example.profile_api.service;

import com.example.profile_api.model.Koi;
import com.example.profile_api.repository.KoiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KoiService {

    private final KoiRepository koiRepository;

    @Autowired
    public KoiService(KoiRepository koiRepository) {
        this.koiRepository = koiRepository;
    }

    // Tạo mới một cá Koi
    public Koi createKoi(Koi koi) {
        return koiRepository.save(koi);
    }

    // Lấy danh sách tất cả cá Koi
    public List<Koi> getAllKoi() {
        return koiRepository.findAll();
    }

    // Lấy thông tin cá Koi theo ID
    public Optional<Koi> getKoiById(Integer koiID) {
        return koiRepository.findById(koiID);
    }

    // Cập nhật thông tin cá Koi
    public Koi updateKoi(Integer koiID, Koi koiDetails) {
        Koi koi = koiRepository.findById(koiID)
                .orElseThrow(() -> new RuntimeException("Koi not found with id " + koiID));

        koi.setName(koiDetails.getName());
        koi.setSpecies(koiDetails.getSpecies());
        koi.setColor(koiDetails.getColor());
        koi.setWeight(koiDetails.getWeight());

        return koiRepository.save(koi);
    }

    // Xóa cá Koi theo ID
    public void deleteKoi(Integer koiID) {
        koiRepository.deleteById(koiID);
    }
}