package com.example.profile_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Koi")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Koi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "koiID")
    private Integer koiID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "species", nullable = false)
    private String species;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "weight", nullable = false)
    private Double weight;

    // Quan hệ với bảng Booking
    @OneToMany(mappedBy = "koi", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;


}
