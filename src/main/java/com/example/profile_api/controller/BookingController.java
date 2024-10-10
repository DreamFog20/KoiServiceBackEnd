package com.example.profile_api.controller;

import com.example.profile_api.model.Booking;
import com.example.profile_api.model.Feedback;
import com.example.profile_api.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // 1. Tạo một đặt lịch mới
    @PostMapping("/createBooking")
    public String createBooking(@RequestBody Booking booking) {
        bookingService.createBooking(booking);
        return "Booking created successfully!";
    }

    // 2. Cập nhật trạng thái đặt lịch
    @PutMapping("/updateStatus/{id}")
    public String updateBookingStatus(@PathVariable Integer id, @RequestParam String status) {
        bookingService.updateBookingStatus(id, status);
        return "Booking status updated successfully!";
    }

    // 3. Lấy danh sách tất cả các đặt lịch
    @GetMapping("/getAllBookings")
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    // 4. Lấy thông tin đặt lịch theo ID
    @GetMapping("/getBooking/{id}")
    public Booking getBookingById(@PathVariable Integer id) {
        return bookingService.getBookingById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));
    }

    // 5. Xóa một đặt lịch theo ID
    @DeleteMapping("/deleteBooking/{id}")
    public String deleteBooking(@PathVariable Integer id) {
        bookingService.deleteBooking(id);
        return "Booking deleted successfully!";
    }

    // 6. Tạo Feedback cho Booking
    @PostMapping("/createFeedback/{bookingId}")
    public String createFeedback(@PathVariable Integer bookingId,
                                 @RequestBody Feedback feedback) {
        bookingService.createFeedbackForBooking(bookingId, feedback);
        return "Feedback created successfully!";
    }
}