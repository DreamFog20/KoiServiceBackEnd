
package com.example.profile_api.controller;

import com.example.profile_api.model.Service;
import com.example.profile_api.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    // Tạo mới một dịch vụ
    @PostMapping
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        Service newService = serviceService.createService(service);
        return ResponseEntity.ok(newService);
    }

    // Lấy danh sách tất cả dịch vụ
    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        List<Service> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    // Lấy dịch vụ theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable("id") Integer serviceID) {
        return serviceService.getServiceById(serviceID)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cập nhật thông tin dịch vụ
    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(
            @PathVariable("id") Integer serviceID,
            @RequestBody Service serviceDetails) {
        Service updatedService = serviceService.updateService(serviceID, serviceDetails);
        return ResponseEntity.ok(updatedService);
    }

    // Xóa dịch vụ theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable("id") Integer serviceID) {
        serviceService.deleteService(serviceID);
        return ResponseEntity.noContent().build();
    }
}
