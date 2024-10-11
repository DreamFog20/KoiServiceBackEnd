package com.example.profile_api.controller;

import com.example.profile_api.dao.BookingRequest;
import com.example.profile_api.model.Booking;
import com.example.profile_api.model.Feedback;
import com.example.profile_api.model.Veterian;
import com.example.profile_api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final ServiceService serviceService;
    private final VeterianService veterianService; // Inject VeterianService
    private final PaymentService paymentService;

    @Autowired
    public BookingController(BookingService bookingService, UserService userService,
                             ServiceService serviceService, VeterianService veterianService, PaymentService paymentService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.serviceService = serviceService;
        this.veterianService = veterianService;
        this.paymentService = paymentService;
    }

    @PostMapping("/createBooking")
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        try {
            // Kiểm tra xem User có tồn tại không
            userService.getUserById(booking.getUser().getUserID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người dùng không tồn tại."));
            // Kiểm tra xem Payment có tồn tại không
            PaymentService.getPaymentById(booking.getPayment().getPaymentID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment với ID " + booking.getPayment().getPaymentID() + " không tồn tại."));


            // Kiểm tra xem Service có tồn tại không
            serviceService.getServiceById(booking.getService().getServiceID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dịch vụ không tồn tại."));

            // Nếu có Veterian mới, lưu Veterian trước
            if (booking.getVet() != null && booking.getVet().getVetID() == null) {
                Veterian savedVet = veterianService.createVeterian(booking.getVet());
                booking.setVet(savedVet);
            }

            bookingService.createBooking(booking);
            return ResponseEntity.ok("Booking created successfully!");

        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                org.hibernate.exception.ConstraintViolationException cve = (org.hibernate.exception.ConstraintViolationException) ex.getCause();

                if (cve.getConstraintName() != null) {
                    // Xử lý các ràng buộc cụ thể
                    switch (cve.getConstraintName()) {

                        case "FK__Booking__koiID__52593CB8":
                            return ResponseEntity.badRequest().body("Koi với ID " + booking.getKoi().getKoiID() + " không tồn tại.");
                        case "FK__Booking__service__534D60F1":
                            return ResponseEntity.badRequest().body("Dịch vụ với ID " + booking.getService().getServiceID() + " không tồn tại.");
                        case "FK__Booking__payment__5441852A":
                            return ResponseEntity.badRequest().body("Payment với ID " + booking.getPayment().getPaymentID() + " không tồn tại.");

                        default:
                            // Xử lý các ràng buộc khác hoặc trả về thông báo lỗi chung
                            return ResponseEntity.badRequest().body("Error: Database constraint violation.");
                    }
                } else {
                    // Xử lý ConstraintViolationException không có tên ràng buộc
                    return ResponseEntity.badRequest().body("Error: Database constraint violation.");
                }
            } else {
                // Xử lý các loại DataIntegrityViolationException khác
                ex.printStackTrace(); // In ra stack trace để debug
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Lỗi tạo booking: " + ex.getMessage());
            }
        }
    }

    // 2. Cập nhật trạng thái đặt lịch
    @PutMapping("/updateStatus/{id}")
    public String updateBookingStatus(@PathVariable Integer id, @RequestParam String status) {
        // Kiểm tra trạng thái hợp lệ nếu cần
        if (status == null || status.isEmpty()) {
            return "Error: Status cannot be null or empty.";
        }
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
        // Kiểm tra feedback không null
        if (feedback == null) {
            return "Error: Feedback cannot be null.";
        }
        bookingService.createFeedbackForBooking(bookingId, feedback);
        return "Feedback created successfully!";
    }

    @PostMapping("/availableVets")
    public ResponseEntity<List<Veterian>> getAvailableVets(@RequestBody BookingRequest request) {
        try {
            List<Veterian> availableVets = bookingService.findAvailableVets(request.getServiceType(), request.getDate(), request.getTimeSlot());
            return new ResponseEntity<>(availableVets, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Or a more specific error
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Bac si vao kham benh
    @PostMapping("/assignVet/{bookingId}")
    public ResponseEntity<Booking> assignVetToBooking(
            @PathVariable Integer bookingId,
            @RequestParam Integer vetId) {
        try {
            Booking updatedBooking = bookingService.assignVetToBooking(bookingId, vetId);
            return ResponseEntity.ok(updatedBooking);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}


