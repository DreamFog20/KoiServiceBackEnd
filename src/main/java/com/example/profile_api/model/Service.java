package com.example.profile_api.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Service")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="serviceID")
    private Integer serviceID;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="basePrice", nullable = false)
    private Double basePrice;

    @Column(name="serviceType")
    private String serviceType;

    @Column(name="duration")
    private Integer duration;


}
