
package com.example.profile_api.controller;

import com.example.profile_api.model.Feedback;
import com.example.profile_api.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "http://localhost:5173")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // 1. Tạo một Feedback mới
    @PostMapping("/create")
    public Feedback createFeedback(@RequestBody Feedback feedback) {
        return feedbackService.createFeedback(feedback);
    }

    // 2. Lấy danh sách tất cả các Feedback
    @GetMapping
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    // 3. Lấy thông tin Feedback theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Integer id) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
        if (feedback.isPresent()) {
            return ResponseEntity.ok(feedback.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 4. Cập nhật thông tin Feedback
    @PutMapping("/update/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Integer id, @RequestBody Feedback feedbackDetails) {
        try {
            Feedback updatedFeedback = feedbackService.updateFeedback(id, feedbackDetails);
            return ResponseEntity.ok(updatedFeedback);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. Xóa một Feedback theo ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Integer id) {
        try {
            feedbackService.deleteFeedback(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
