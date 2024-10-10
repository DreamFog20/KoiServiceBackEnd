package com.example.profile_api.repository;

import com.example.profile_api.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    // Bạn có thể thêm các phương thức tùy chỉnh ở đây nếu cần
}