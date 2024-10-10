package com.example.profile_api.repository;



import com.example.profile_api.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    // Bạn có thể thêm các phương thức tùy chỉnh ở đây nếu cần
}

