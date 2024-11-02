package com.example.profile_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrescriptionDTO {
    private Integer prescriptionID;
    private Integer vetID;
    private Integer recordID;
    private String medication;
    private String instruction;
}
