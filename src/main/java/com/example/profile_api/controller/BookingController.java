package com.example.profile_api.controller;

import com.example.profile_api.dao.AvailableVetRequest;
import com.example.profile_api.dto.BookingCreateDTO;
import com.example.profile_api.dto.BookingDTO;
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
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/bookings")
public class BookingController {

    private  BookingService bookingService;
    private  UserService userService;
    private  ServiceService serviceService;
    private  VeterianService veterianService;
    private  PaymentService paymentService;
    private VetScheduleService vetScheduleService;
    @Autowired
    public BookingController(BookingService bookingService, UserService userService, ServiceService serviceService,
                             VeterianService veterianService, PaymentService paymentService,VetScheduleService vetScheduleService)
    {
        this.bookingService = bookingService;
        this.userService = userService;
        this.serviceService = serviceService;
        this.veterianService = veterianService;
        this.paymentService = paymentService;
        this.vetScheduleService =  vetScheduleService;

    }


    @PostMapping(value = "/create")
    public ResponseEntity<?> createBooking(@RequestBody BookingCreateDTO bookingDTO) {
        try {
            // Lấy các đối tượng từ database
            User user = userService.getUserById(bookingDTO.getUserID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy người dùng"));
            Service service = serviceService.getServiceById(bookingDTO.getServiceID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy dịch vụ"));
            VetSchedule vetSchedule = vetScheduleService.getVetScheduleById(bookingDTO.getScheduleID());

            Veterian newVet = new Veterian();
            newVet.setVetID(vetSchedule.getVeterian().getVetID());

            // Tạo booking
            Booking booking = new Booking();
            booking.setStatus(bookingDTO.getStatus());
            booking.setUser(user);
            booking.setVet(newVet);
            booking.setService(service);
            booking.setDate(bookingDTO.getDate());
            booking.setVetSchedule(vetSchedule);

            Booking savedBooking = bookingService.createBooking(booking);

            return ResponseEntity.created(URI.create("/api/bookings/" + savedBooking.getBookingID())).build();

        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        } catch (Exception ex) {
            ex.printStackTrace();
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
            Service service = serviceService.getServiceById(bookingDto.getServiceID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dịch vụ không tồn tại."));

            // Tạo đối tượng Booking
            Booking booking = new Booking();
            booking.setUser(user);
            booking.setService(service);
            booking.setDate(bookingDto.getDate());
            // ... gán các thuộc tính khác từ bookingDto
            LocalDate localDate = bookingDto.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            VetSchedule selectedSchedule = null;

            // Nếu người dùng chọn bác sĩ
            if (bookingDto.getVetID() != null) {
                // Tìm lịch trình của bác sĩ được chọn
                List<VetSchedule> availableSchedules = bookingService.findAvailableSlotsByVetAndDate(bookingDto.getVetID(), localDate);

                // Nếu có khung giờ cụ thể
                if (bookingDto.getSlot() != null) {
                    // Tìm lịch trình phù hợp với khung giờ
                    selectedSchedule = availableSchedules.stream()
                            .filter(schedule -> schedule.getSlot().equals(bookingDto.getSlot())) // So sánh với slot được tính toán
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khung giờ không khả dụng."));
                } else {
                    // Chọn ngẫu nhiên một lịch trình của bác sĩ
                    if (!availableSchedules.isEmpty()) {
                        Random random = new Random();
                        selectedSchedule = availableSchedules.get(random.nextInt(availableSchedules.size()));
                    } else {
                        return ResponseEntity.badRequest().body("Bác sĩ không rảnh vào ngày này.");
                    }
                }
            } else {
                // Tìm lịch trình bác sĩ thú y rảnh
                List<VetSchedule> availableSchedules = bookingService.findAvailableSchedulesByDate(localDate);
                if (availableSchedules.isEmpty()) {
                    return ResponseEntity.badRequest().body("Không có bác sĩ rảnh vào ngày này.");
                }

                // Chọn ngẫu nhiên một lịch trình
                Random random = new Random();
                selectedSchedule = availableSchedules.get(random.nextInt(availableSchedules.size()));
            }

            booking.setVetSchedule(selectedSchedule);

            // Lưu booking
            Booking savedBooking = bookingService.createBooking(booking);

            // Tạo DTO để trả về
            BookingResponseDto responseDto = new BookingResponseDto();


            return ResponseEntity.created(URI.create("/api/bookings/" + savedBooking.getBookingID()))
                    .body(responseDto);

        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi tạo booking: " + e.getMessage());
        }
    }
    @GetMapping("/status")
    public List<Booking> getBookingsByStatus(@RequestParam(required
            = false) String status) {
        if (status != null) {
            return bookingService.getBookingsByStatus(status);
        } else {
            return bookingService.getAllBookings();
        }
    }
    @GetMapping("/history/koi/{koiId}")
    public ResponseEntity<List<Booking>> getBookingHistoryByKoiId(@PathVariable Integer  koiId) {
        List<Booking> bookings = bookingService.getBookingHistoryByKoiId(koiId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/history/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingHistoryByUserId(@PathVariable Integer  userId) {
        List<Booking> bookings = bookingService.getBookingHistoryByUserId(userId);
        return ResponseEntity.ok(bookings);
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
    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<List<BookingDTO>> getBookingsByVetScheduleId(@PathVariable Integer scheduleId) {
        List<BookingDTO> bookingDTOs = bookingService.getBookingsByVetScheduleIdDTO(scheduleId);
        return ResponseEntity.ok(bookingDTOs);
    }
}