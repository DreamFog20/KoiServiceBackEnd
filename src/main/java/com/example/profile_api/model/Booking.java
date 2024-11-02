
package com.example.profile_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookingID")
    private Integer bookingID;

    @Column(name = "status", nullable = true)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    // Quan hệ với bảng Koi (có thể bỏ nếu không cần thiết)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "koiID", nullable = true)
    private Koi koi;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "vetID", nullable = true)
    private Veterian vet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "serviceID", nullable = false)
    private Service service;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE) // Sử dụng annotation @Temporal để chỉ định kiểu dữ liệu Date
    private Date date; // Sử dụng LocalDate

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "paymentID", nullable = true)
    private Payment payment;

    // Quan hệ một-một với bảng Feedback (có thể bỏ nếu không cần thiết)
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Feedback feedback;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduleID", nullable = true)
    private VetSchedule vetSchedule;
}
