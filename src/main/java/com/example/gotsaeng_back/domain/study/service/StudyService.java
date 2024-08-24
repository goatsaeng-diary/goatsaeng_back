package com.example.gotsaeng_back.domain.study.service;

import com.example.gotsaeng_back.domain.study.dto.StudyDto;
import com.example.gotsaeng_back.domain.study.entity.Study;
import com.example.gotsaeng_back.global.gptapi.dto.GPTResponseDto;

import java.time.LocalDate;

public interface StudyService {
    void save(GPTResponseDto gptResponseDto, String prompt);
    Study findByToday(LocalDate today);
    boolean existsByToday(LocalDate today);
    boolean isCorrect(StudyDto studyDto,Study study);
}
