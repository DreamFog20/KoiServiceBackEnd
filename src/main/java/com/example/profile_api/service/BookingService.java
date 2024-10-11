package com.example.profile_api.service;

import com.example.profile_api.model.Booking;
import com.example.profile_api.model.Feedback;
import com.example.profile_api.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // 1. Tạo mới một Booking
    public Booking createBooking(Booking booking) {
        // Kiểm tra nếu vet là null, có thể có logic khác tùy theo yêu cầu
        if (booking.getVet() == null) {
            // Xử lý trường hợp không có bác sĩ thú y
        }
        return bookingRepository.save(booking);
    }


    // 2. Lấy danh sách tất cả các Booking
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // 3. Lấy thông tin Booking theo ID
    public Optional<Booking> getBookingById(Integer bookingID) {
        return bookingRepository.findById(bookingID);
    }

    // 4. Cập nhật trạng thái Booking
    public Booking updateBookingStatus(Integer bookingID, String status) {
        Booking booking = bookingRepository.findById(bookingID)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingID));

        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    // 5. Xóa Booking theo ID
    public void deleteBooking(Integer bookingID) {
        Booking booking = bookingRepository.findById(bookingID)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingID));

        bookingRepository.delete(booking);
    }

    // 6. Tạo Feedback cho Booking
    public Feedback createFeedbackForBooking(Integer bookingID, Feedback feedback) {
        Booking booking = bookingRepository.findById(bookingID)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingID));

        feedback.setBooking(booking);
        booking.setFeedback(feedback);
        bookingRepository.save(booking); // Cập nhật Booking để liên kết với Feedback

        return feedback; // Trả về Feedback đã được lưu
    }
}
