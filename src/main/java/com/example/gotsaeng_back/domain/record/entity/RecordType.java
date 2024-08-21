package com.example.gotsaeng_back.domain.record.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "record_types")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//기본으로 제공하는 기록 저장하는 로직 -> 기록 분류, 기록 단위
public class RecordType {
    @Id
    @Column(name = "record_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordTypeId;

    @Column(nullable = false, name = "type_name")
    private String typeName;

    @Column(nullable = false)
    private String unit;
}