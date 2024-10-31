
package com.example.profile_api.repository;


import com.example.profile_api.dto.ServiceRevenue;
import com.example.profile_api.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND (:date IS NULL OR b.date = :date)")
    List<Booking> findByUserUserIDAndDate(@Param("userId") Integer userId, @Param("date") Optional<LocalDate> date);

    @Query("SELECT b FROM Booking b WHERE b.vet.vetID = :vetId AND b.date = :date")
    List<Booking> findByVetVetIDAndDate(@Param("vetId") Integer vetId, @Param("date") LocalDate date);

    List<Booking> findByStatus(String status);
    @Query("SELECT b FROM Booking b WHERE b.koi.koiId = :koiId") // Truy vấn JPQL rõ ràng
    List<Booking> findBookingsByKoiId(@Param("koiId") Integer koiId);


    @Query("SELECT new com.example.profile_api.dto.ServiceRevenue(s.name, SUM(s.basePrice)) " +
            "FROM Service s " +
            "JOIN Booking b ON s.serviceID = b.service.serviceID " +  // Sửa b.serviceID thành b.service.serviceID
            "JOIN Payment p ON b.payment.paymentID = p.paymentID " + // Sửa b.paymentID thành b.payment.paymentID
            "WHERE p.paymentDate >= :startDate AND p.paymentDate < :endDate " +
            "GROUP BY s.name")
    List<ServiceRevenue> getServiceRevenue(LocalDate startDate, LocalDate endDate);
    List<Booking> findByUserId(Integer userId);
    List<Booking> findByVetSchedule_ScheduleID(Integer scheduleId);

}
