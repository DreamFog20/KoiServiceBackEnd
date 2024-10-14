
package com.example.profile_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Record")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RecordID")
    private Integer recordID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KoiID", nullable = false)
    private Koi koi; // Quan hệ với bảng Koi (có thể là bảng chứa thông tin về cá Koi)

    @Column(name = "HealthStatus", nullable = false)
    private String healthStatus; // Trạng thái sức khỏe của cá Koi

    @Column(name = "Diagnosis", nullable = false)
    private String diagnosis; // Chuẩn đoán của bác sĩ
}
