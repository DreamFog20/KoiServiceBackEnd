package com.example.profile_api.service;

import com.example.profile_api.model.Service;
import com.example.profile_api.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }
    public List<Service> getServicesByType(String serviceType) {
        return serviceRepository.findByServiceType(serviceType);
    }

    public List<Service> searchServicesByName(String name) {
        return serviceRepository.findByNameContaining(name);
    }
    // Tạo mới một dịch vụ
    public Service createService(Service service) {
        return serviceRepository.save(service);
    }

    // Lấy danh sách tất cả các dịch vụ
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    // Lấy thông tin dịch vụ theo ID
    public Optional<Service> getServiceById(Integer serviceID) {
        return serviceRepository.findById(serviceID);
    }

    // Cập nhật thông tin dịch vụ
    public Service updateService(Integer serviceID, Service serviceDetails) {
        Service service = serviceRepository.findById(serviceID)
                .orElseThrow(() -> new RuntimeException("Service not found with id " + serviceID));

        service.setName(serviceDetails.getName());
        service.setDescription(serviceDetails.getDescription());
        service.setBasePrice(serviceDetails.getBasePrice());
        service.setServiceType(serviceDetails.getServiceType());
        service.setDuration(serviceDetails.getDuration());

        return serviceRepository.save(service);
    }

    // Xóa dịch vụ theo ID
    public void deleteService(Integer serviceID) {
        serviceRepository.deleteById(serviceID);
    }
}
