    package com.example.profile_api.model;

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
    public class Booking {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "bookingID")
        private Integer bookingID;

        @Column(name = "status", nullable = false)
        private String status; // Trạng thái của Booking

        // Quan hệ với bảng Users
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "userID", nullable = false)
        private User user;

        // Quan hệ với bảng Koi
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "koiID", nullable = false)
        private Koi koi;

        // Quan hệ với bảng Veterian (bác sĩ thú y)
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "vetID", nullable = true) // Cho phép vetID là null
        private Veterian vet;

        // Quan hệ với bảng Service
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "serviceID", nullable = false)
        private Service service;

        @Column(name = "date", nullable = false)
        @Temporal(TemporalType.DATE)
        private Date date;

        // Quan hệ với bảng Payment
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "paymentID", nullable = false)
        private Payment payment;

        // Quan hệ một-một với bảng Feedback
        @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private Feedback feedback; // Quan hệ một-một với bảng Feedback
    }

