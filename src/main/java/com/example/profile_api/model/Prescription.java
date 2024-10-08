package com.example.profile_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Prescription")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PrescriptionID")
    private Integer prescriptionID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VetID", nullable = false)
    private Veterian veterian; // Quan hệ với bảng Veterian (Bác sĩ thú y)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RecordID", nullable = false)
    private Record record; // Quan hệ với bảng Record (Hồ sơ y tế của cá Koi)

    @Column(name = "Medication", nullable = false)
    private String medication; // Thuốc được kê

    @Column(name = "Instruction", nullable = false)
    private String instruction; // Hướng dẫn sử dụng thuốc

}