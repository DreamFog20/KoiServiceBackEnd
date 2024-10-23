package com.example.profile_api.service;

import com.example.profile_api.dto.ServiceRevenue;
import com.example.profile_api.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private BookingRepository bookingRepository; // Thêm BookingRepository

    public List<ServiceRevenue> getServiceRevenue(LocalDate startDate, LocalDate endDate) {
        return bookingRepository.getServiceRevenue(startDate, endDate);
    }
    // ... các phương thức khác
}