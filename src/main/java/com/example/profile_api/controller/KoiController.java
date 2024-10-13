package com.example.profile_api.controller;

import com.example.profile_api.model.Koi;
import com.example.profile_api.service.KoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/koi")
@CrossOrigin(origins = "http://localhost:5173")
public class KoiController {

    private final KoiService koiService;

    @Autowired
    public KoiController(KoiService koiService) {
        this.koiService = koiService;
    }

    // Tạo mới một cá Koi
    @PostMapping
    public Koi createKoi(@RequestBody Koi koi) {
        return koiService.createKoi(koi);
    }

    // Lấy danh sách tất cả cá Koi
    @GetMapping
    public List<Koi> getAllKoi() {
        return koiService.getAllKoi();
    }

    // Lấy thông tin cá Koi theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Koi> getKoiById(@PathVariable("id") Integer id) {
        return koiService.getKoiById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cập nhật thông tin cá Koi
    @PutMapping("/{id}")
    public ResponseEntity<Koi> updateKoi(@PathVariable("id") Integer id, @RequestBody Koi koiDetails) {
        Koi updatedKoi = koiService.updateKoi(id, koiDetails);
        return ResponseEntity.ok(updatedKoi);
    }

    // Xóa cá Koi theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKoi(@PathVariable("id") Integer id) {
        koiService.deleteKoi(id);
        return ResponseEntity.noContent().build();
    }
}