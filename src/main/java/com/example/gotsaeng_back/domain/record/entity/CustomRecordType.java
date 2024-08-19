package com.example.gotsaeng_back.domain.record.entity;

import com.example.gotsaeng_back.domain.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "custom_record_types")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomRecordType {

    @Id
    @Column(name = "custom_record_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customRecordTypeId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, name = "type_name")
    private String typeName;

    private String unit;

    @Column(columnDefinition = "JSON")
    private String fields;
}