
package com.example.profile_api.repository;


import com.example.profile_api.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT b FROM Booking b WHERE b.vet.vetID = :vetId AND b.date = :date")
    List<Booking> findByVetVetIDAndDate(@Param("vetId") Integer vetId, @Param("date") LocalDate date);

    List<Booking> findByUserUserIDAndDate(Integer userId, LocalDate date);

    List<Booking> findByStatus(String status);

    // Có thể thêm các phương thức custom khác nếu cần
}
