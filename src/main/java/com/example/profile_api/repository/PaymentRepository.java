
package com.example.profile_api.repository;

import com.example.profile_api.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

        @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Payment p WHERE p.vnp_TransactionNo = :vnp_TransactionNo")
        boolean existsByVnp_TransactionNo(String vnp_TransactionNo);

}
