package com.example.gotsaeng_back.domain.record.entity;

import com.example.gotsaeng_back.domain.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Records")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Record {
    @Id
    @Column(name = "record_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "record_type_id", nullable = false)
    private RecordType recordType;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate date = LocalDate.now();

    @Column(columnDefinition = "JSON")
    private String value;
}