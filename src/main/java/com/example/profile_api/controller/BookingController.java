package com.example.profile_api.controller;

import com.example.profile_api.dao.AvailableVetRequest;
import com.example.profile_api.dto.BookingRequestDto;
import com.example.profile_api.dto.BookingResponseDto;
import com.example.profile_api.model.*;
import com.example.profile_api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final ServiceService serviceService;
    private final VeterianService veterianService;
    private final PaymentService paymentService;

    @Autowired
    public BookingController(BookingService bookingService, UserService userService,
                             ServiceService serviceService, VeterianService veterianService,
                             PaymentService paymentService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.serviceService = serviceService;
        this.veterianService = veterianService;
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        try {
            // Kiểm tra xem User, Payment, Service có tồn tại không
            userService.getUserById(booking.getUser().getUserID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người dùng không tồn tại."));
            paymentService.getPaymentById(booking.getPayment().getPaymentID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment không tồn tại."));
            serviceService.getServiceById(booking.getService().getServiceID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dịch vụ không tồn tại."));

            // Nếu có Veterian mới, lưu Veterian trước
            if (booking.getVet() != null && booking.getVet().getVetID() == null) {
                Veterian savedVet = veterianService.createVeterian(booking.getVet());
                booking.setVet(savedVet);
            }

            bookingService.createBooking(booking);
            return ResponseEntity.ok("Booking created successfully!");

        } catch (Exception ex) {
            ex.printStackTrace(); // In ra stack trace để debug
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi tạo booking: " + ex.getMessage());
        }
    }

    @PutMapping("/status/{id}")
    public String updateBookingStatus(@PathVariable Integer id, @RequestParam String status) {
        if (status == null || status.isEmpty()) {
            return "Error: Status cannot be null or empty.";
        }
        bookingService.updateBookingStatus(id, status);
        return "Booking status updated successfully!";
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Integer id) {
        return bookingService.getBookingById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));
    }

    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable Integer id) {
        bookingService.deleteBooking(id);
        return "Booking deleted successfully!";
    }

    @PostMapping("/{bookingId}/feedback")
    public String createFeedback(@PathVariable Integer bookingId,
                                 @RequestBody Feedback feedback) {
        if (feedback == null) {
            return "Error: Feedback cannot be null.";
        }
        bookingService.createFeedbackForBooking(bookingId, feedback);
        return "Feedback created successfully!";
    }

    @PostMapping("/available-vets")
    public ResponseEntity<List<VetSchedule>> getAvailableVetsByDate(@RequestBody AvailableVetRequest request) {
        try {
            List<VetSchedule> availableSchedules = bookingService.findAvailableSchedulesByDate(request.getDate());
            return new ResponseEntity<>(availableSchedules, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/random-vet")
    public ResponseEntity<?> createBookingWithRandomVet(@RequestBody BookingRequestDto bookingDto) {
        try {
            // Lấy User, Payment, Service từ database dựa trên ID
            User user = userService.getUserById(bookingDto.getUserID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người dùng không tồn tại."));
            Payment payment = paymentService.getPaymentById(bookingDto.getPaymentID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment không tồn tại."));
            Service service = serviceService.getServiceById(bookingDto.getServiceID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dịch vụ không tồn tại."));

            // Tạo đối tượng Booking
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setPayment(payment);
            booking.setService(service);
            booking.setDate(bookingDto.getDate());
            // ... gán các thuộc tính khác từ bookingDto
            LocalDate localDate = bookingDto.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            // Tìm lịch trình bác sĩ thú y rảnh
            List<VetSchedule> availableSchedules = bookingService.findAvailableSchedulesByDate(localDate);
            if (availableSchedules.isEmpty()) {
                return ResponseEntity.badRequest().body("Không có bác sĩ rảnh vào ngày này.");
            }

            // Chọn ngẫu nhiên một lịch trình
            Random random = new Random();
            VetSchedule randomSchedule = availableSchedules.get(random.nextInt(availableSchedules.size()));
            booking.setVetSchedule(randomSchedule);

            // Lưu booking
            Booking savedBooking = bookingService.createBooking(booking);

            // Tạo DTO để trả về
            BookingResponseDto responseDto = new BookingResponseDto();
            responseDto.setBookingID(savedBooking.getBookingID());
            responseDto.setStatus(savedBooking.getStatus());
            responseDto.setUser(savedBooking.getUser());
            responseDto.setService((org.springframework.stereotype.Service) savedBooking.getService());
            responseDto.setDate(savedBooking.getDate());
            responseDto.setVetSchedule(savedBooking.getVetSchedule());

            return ResponseEntity.created(URI.create("/api/bookings/" + savedBooking.getBookingID()))
                    .body(responseDto);

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi tạo booking: " + e.getMessage());
        }
    }

    @PostMapping("/{bookingId}/vet/{vetId}")
    public ResponseEntity<Booking> assignVetToBooking(
            @PathVariable Integer bookingId,
            @PathVariable Integer vetId) {
        try {
            Booking updatedBooking = bookingService.assignVetToBooking(bookingId, vetId);
            return ResponseEntity.ok(updatedBooking);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}