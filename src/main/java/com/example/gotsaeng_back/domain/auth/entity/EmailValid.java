package com.example.gotsaeng_back.domain.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="email_valid")
@Getter
@Setter
public class EmailValid {

    @Id
    private String email;

    private String code;
}
