
package com.example.profile_api.controller;

import com.example.profile_api.dto.ServiceRevenue;
import com.example.profile_api.model.Service;
import com.example.profile_api.service.ReportService;
import com.example.profile_api.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/services")
public class ServiceController {
    private final ReportService reportService;
    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ReportService reportService, ServiceService serviceService) {
        this.reportService = reportService;
        this.serviceService = serviceService;
    }

    // Tạo mới một dịch vụ
    @PostMapping
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        Service newService = serviceService.createService(service);
        return ResponseEntity.ok(newService);
    }

    // Lấy danh sách tất cả dịch vụ
    @GetMapping("/all")
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
    @GetMapping("/type/{serviceType}")
    public ResponseEntity<List<Service>> getServicesByType(@PathVariable String serviceType) {
        List<Service> services = serviceService.getServicesByType(serviceType);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Service>> searchServicesByName(@RequestParam String name) {
        List<Service> services = serviceService.searchServicesByName(name);
        return ResponseEntity.ok(services);
    }
    //Báo cáo tài chính, doanh thu theo dịch vụ
    @GetMapping("/service-revenue")
    public ResponseEntity<List<ServiceRevenue>> getServiceRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<ServiceRevenue>
        revenue = reportService.getServiceRevenue(startDate, endDate);
        return ResponseEntity.ok(revenue);
    }
}
