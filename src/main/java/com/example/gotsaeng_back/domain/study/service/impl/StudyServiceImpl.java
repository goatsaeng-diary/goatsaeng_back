package com.example.gotsaeng_back.domain.study.service.impl;

import com.example.gotsaeng_back.domain.study.entity.Study;
import com.example.gotsaeng_back.domain.study.repository.StudyRepository;
import com.example.gotsaeng_back.domain.study.service.StudyService;
import com.example.gotsaeng_back.global.gptapi.dto.GPTResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {
    private final StudyRepository studyRepository;

    @Override
    public void save(GPTResponseDto gptResponseDto, String prompt) {
        Study study = new Study();
        study.setQuestion(gptResponseDto.getResponse());
        study.setAnswer(prompt);
        study.setPoint(100L);
        studyRepository.save(study);
    }

    @Override
    public Study findByToday(LocalDate today) {
        Study study = studyRepository.findByCreatedAt(today);
        return study;
    }

    @Override
    public boolean existsByToday(LocalDate today) {
        return studyRepository.existsByCreatedAt(today);
    }

    @Override
    public boolean getAnswer(String answer) {
        Study study = studyRepository.findByCreatedAt(LocalDate.now());
        if(study.getAnswer().equals(answer)){
            return true;
        }
        return false;
    }
}
