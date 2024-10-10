package com.example.profile_api.service;


import com.example.profile_api.model.Feedback;
import com.example.profile_api.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    // 1. Tạo mới một Feedback
    public Feedback createFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    // 2. Lấy danh sách tất cả các Feedback
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    // 3. Lấy thông tin Feedback theo ID
    public Optional<Feedback> getFeedbackById(Integer feedbackID) {
        return feedbackRepository.findById(feedbackID);
    }

    // 4. Cập nhật thông tin Feedback
    public Feedback updateFeedback(Integer feedbackID, Feedback feedbackDetails) {
        Feedback feedback = feedbackRepository.findById(feedbackID)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id " + feedbackID));

        feedback.setRating(feedbackDetails.getRating());
        feedback.setComment(feedbackDetails.getComment());
        feedback.setCreatedAt(feedbackDetails.getCreatedAt());
        feedback.setBooking(feedbackDetails.getBooking());
        // Cập nhật các thuộc tính khác nếu có

        return feedbackRepository.save(feedback);
    }

    // 5. Xóa một Feedback theo ID
    public void deleteFeedback(Integer feedbackID) {
        Feedback feedback = feedbackRepository.findById(feedbackID)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id " + feedbackID));
        feedbackRepository.delete(feedback);
    }
}
