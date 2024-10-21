
package com.example.profile_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Payment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentID")
    private Integer paymentID;

    @Column(name = "totalAmount", nullable = false)
    private Double totalAmount;

//    @Column(name = "paymentMethod", nullable = false)
//    private String paymentMethod;

    @Column(name = "paymentDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    @Column(name = "vnp_TransactionNo")
    private String vnp_TransactionNo;
    @Column(name="BankCode")
    private String bankCode;
    @Column(name = "status", nullable = false)
    private String status;

    // Quan hệ với bảng Booking
    @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY)
    private List<Booking> bookings;
}
