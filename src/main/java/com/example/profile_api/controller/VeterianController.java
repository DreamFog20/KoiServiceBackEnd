package com.example.profile_api.controller;


import com.example.profile_api.model.Veterian;
import com.example.profile_api.service.VeterianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/veterian")
public class VeterianController {

    private final VeterianService veterianService;

    @Autowired
    public VeterianController(VeterianService veterianService) {
        this.veterianService = veterianService;
    }

    // 1. Tạo một Veterian mới
    @PostMapping("/create")
    public Veterian createVeterian(@RequestBody Veterian veterian) {
        return veterianService.createVeterian(veterian);
    }

    // 2. Lấy danh sách tất cả các Veterian
    @GetMapping("/all")
    public List<Veterian> getAllVeterians() {
        return veterianService.getAllVeterians();
    }

    // 3. Lấy thông tin Veterian theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Veterian> getVeterianById(@PathVariable Integer id) {
        Optional<Veterian> veterian = veterianService.getVeterianById(id);
        if (veterian.isPresent()) {
            return ResponseEntity.ok(veterian.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 4. Cập nhật thông tin Veterian
    @PutMapping("/update/{id}")
    public ResponseEntity<Veterian> updateVeterian(@PathVariable Integer id, @RequestBody Veterian veterianDetails) {
        try {
            Veterian updatedVeterian = veterianService.updateVeterian(id, veterianDetails);
            return ResponseEntity.ok(updatedVeterian);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. Xóa một Veterian theo ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVeterian(@PathVariable Integer id) {
        try {
            veterianService.deleteVeterian(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
