
package com.example.profile_api.service;

import com.example.profile_api.model.Booking;
import com.example.profile_api.model.Feedback;
import com.example.profile_api.model.VetSchedule;
import com.example.profile_api.model.Veterian;
import com.example.profile_api.repository.BookingRepository;
import com.example.profile_api.repository.VetScheduleRepository;
import com.example.profile_api.repository.VeterianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {
    @Autowired
    private VeterianRepository veterianRepository;

    @Autowired
    private VetScheduleRepository vetScheduleRepository;
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
    //7 Tìm kiếm bác sĩ có chuyên môn phù hợp với loại dịch vụ
    public List<Veterian> findAvailableVetsByDate(LocalDate date) {
        List<VetSchedule> schedules = vetScheduleRepository.findByScheduleDateAndAvailability(date, true);
        List<Integer> vetIds = schedules.stream()
                .map(schedule -> schedule.getVeterian().getVetID())
                .distinct()
                .collect(Collectors.toList());
        return veterianRepository.findAllById(vetIds);
    }


    public List<VetSchedule> findAvailableSlotsByVetAndDate(Integer vetId, LocalDate date) {
        List<VetSchedule> schedules = vetScheduleRepository.findByVeterianVetIDAndScheduleDateAndAvailability(vetId, date, true);
        List<Booking> bookings = bookingRepository.findByVetVetIDAndDate(vetId, date);

        List<Integer> bookedScheduleIds = bookings.stream()
                .filter(booking -> booking.getVetSchedule() != null)
                .map(booking -> booking.getVetSchedule().getScheduleID())
                .collect(Collectors.toList());

        return schedules.stream()
                .filter(schedule -> !bookedScheduleIds.contains(schedule.getScheduleID()))
                .collect(Collectors.toList());
    }
    // 8. Kiểm tra xem Booking có tồn tại không
    public Booking assignVetToBooking(Integer bookingId, Integer vetId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

        //  Kiểm tra xem Veterian có tồn tại không
        Veterian vet = veterianRepository.findById(vetId)
                .orElseThrow(() -> new IllegalArgumentException("Veterian not found with ID: " + vetId));

        //  Phân công bác sĩ cho đơn đặt lịch
        booking.setVet(vet);
        return bookingRepository.save(booking);
    }
}
