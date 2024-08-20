package com.example.gotsaeng_back.domain.record.entity;

import com.example.gotsaeng_back.domain.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "record_target")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordTarget {
    @Id
    @Column(name = "target_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long targetId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "record_type_id")
    private RecordType recordType;

    @ManyToOne
    @JoinColumn(name = "custom_record_type_id")
    private CustomRecordType customRecordType;

    @Column(nullable = false, name = "target")
    private Float target;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate date = LocalDate.now();
}