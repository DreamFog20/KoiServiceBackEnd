
package com.example.profile_api.controller;

import com.example.profile_api.config.JwtGeneratorInterface;
import com.example.profile_api.dao.LoginToken;
import com.example.profile_api.model.Booking;
import com.example.profile_api.model.User;
import com.example.profile_api.repository.BookingRepository;
import com.example.profile_api.service.UserService;
import com.example.profile_api.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private BookingRepository bookingRepository;
    private JwtGeneratorInterface jwtGenerator;
    private UserService userService;

    @Autowired
    public UserController(UserServiceImpl userService, JwtGeneratorInterface jwtGenerator){
        this.userService=userService;
        this.jwtGenerator=jwtGenerator;
    }


    @GetMapping
    public ResponseEntity<List<User>> getAllTutorials(@RequestParam(required = false) String title) {
        List<User> users = userService.getALlUser();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUserProfile(@PathVariable int  id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUserProfile(@PathVariable int id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) throws Exception {
        try {
            if (user.getEmail() == null || user.getPassword() == null) {
                throw new Exception("Email or Password is Empty");
            }

            User userData = userService.getUserByEmailAndPassword(user.getEmail(), user.getPassword());

            if (userData == null) {
                throw new Exception("Email or Password is Invalid");
            }

            // Đặt password là null để không gửi về
            userData.setPassword(null);

            // Tạo token
            Map<String, String> token = jwtGenerator.generateToken(user);

            // Gửi thông tin user, bao gồm cả roleId
            LoginToken loginToken = new LoginToken(token.get("token"), userData);
            return new ResponseEntity<>(loginToken, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
    //Lấy lịch sử booking của người dùng (khách hàng):
    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<Booking>> getBookingHistory(@PathVariable Integer id) {
        List<Booking> bookings = bookingRepository.findByUserId(id);
        return ResponseEntity.ok(bookings);
    }
}
