package com.example.profile_api.service;



import com.example.profile_api.model.Payment;
import com.example.profile_api.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // 1. Tạo mới một Payment
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    // 2. Lấy danh sách tất cả các Payment
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // 3. Lấy thông tin Payment theo ID
    public Optional<Payment> getPaymentById(Integer paymentID) {
        return paymentRepository.findById(paymentID);
    }

    // 4. Cập nhật thông tin Payment
    public Payment updatePayment(Integer paymentID, Payment paymentDetails) {
        Payment payment = paymentRepository.findById(paymentID)
                .orElseThrow(() -> new RuntimeException("Payment not found with id " + paymentID));

        payment.setTotalAmount(paymentDetails.getTotalAmount());
        payment.setPaymentMethod(paymentDetails.getPaymentMethod());
        payment.setPaymentDate(paymentDetails.getPaymentDate());
        payment.setStatus(paymentDetails.getStatus());
        // Cập nhật các thuộc tính khác nếu có

        return paymentRepository.save(payment);
    }

    // 5. Xóa một Payment theo ID
    public void deletePayment(Integer paymentID) {
        Payment payment = paymentRepository.findById(paymentID)
                .orElseThrow(() -> new RuntimeException("Payment not found with id " + paymentID));
        paymentRepository.delete(payment);
    }
}
