package com.example.gotsaeng_back.global.gptapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "words")
@Getter
@Setter
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordId;
    @Column(name = "words_name")
    private String wordName;
}
