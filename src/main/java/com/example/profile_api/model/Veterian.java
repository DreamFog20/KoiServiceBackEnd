package com.example.profile_api.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Veterian")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Veterian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vetID")
    private Integer vetID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "specialization", nullable = false)
    private String specialization;

    @OneToMany(  cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> booking;

    @OneToMany(mappedBy = "veterian", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<VetSchedule> vetSchedules;
}
