package com.example.gotsaeng_back.domain.record.entity;

import com.example.gotsaeng_back.domain.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "custom_records")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomRecord {
    @Id
    @Column(name = "custom_record_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customRecordId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "custom_record_type_id", nullable = false)
    private CustomRecordType customRecordType;

    @Column(nullable = false)
    private LocalDate date = LocalDate.now();

    //  json 형식으로 ex) value : {"amount" : 2000}
    @Column(columnDefinition = "JSON", nullable = false)
    private String value;
}
