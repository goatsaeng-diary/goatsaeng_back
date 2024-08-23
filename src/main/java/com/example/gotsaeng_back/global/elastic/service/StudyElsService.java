package com.example.gotsaeng_back.global.elastic.service;

import com.example.gotsaeng_back.global.elastic.index.Study;
import com.example.gotsaeng_back.global.elastic.repository.StudyElsRepository;
import com.example.gotsaeng_back.global.gptapi.dto.GPTRequestDto;
import com.example.gotsaeng_back.global.gptapi.dto.GPTResponseDto;
import com.example.gotsaeng_back.global.gptapi.service.GPTService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StudyElsService {

    private final StudyElsRepository studyElsRepository;
    private final GPTService gptService;

    public void saveStudy(String title, String content) {
        Study study = new Study();
        study.setTitle(title);
        study.setContent(content);
        studyElsRepository.save(study);
    }
    public List<Study> findByContentMatch(String keyword){
        List<Study> list = studyElsRepository.findByContent(keyword);
        String prompt = "";
        for(Study study : list){
            prompt += study.getContent();
        }
        prompt = prompt + "에서" + keyword + "라는말에 답변을 해줘";
        Mono<?> mono = gptService.getGPTResponse(new GPTRequestDto(prompt));
        System.out.println("222"+mono);
        return null;
    }
}
