package com.example.gotsaeng_back.global.elastic.service;

import com.example.gotsaeng_back.global.elastic.index.Study;
import com.example.gotsaeng_back.global.elastic.repository.StudyElsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyElsService {

    private final StudyElsRepository studyElsRepository;

    public void saveStudy(String title, String content) {
        Study study = new Study();
        study.setTitle(title);
        study.setContent(content);
        studyElsRepository.save(study);
    }
}
