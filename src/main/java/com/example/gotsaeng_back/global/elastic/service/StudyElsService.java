package com.example.gotsaeng_back.global.elastic.service;

import com.example.gotsaeng_back.global.elastic.index.Study;
import com.example.gotsaeng_back.global.elastic.repository.StudyElsRepository;
import com.example.gotsaeng_back.global.gptapi.dto.GPTRequestDto;
import com.example.gotsaeng_back.global.gptapi.dto.GPTResponseDto;
import com.example.gotsaeng_back.global.gptapi.service.GPTService;
import com.example.gotsaeng_back.global.response.CustomResponse;
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


    public Mono<GPTResponseDto> findByContentMatch(String keyword) {
        List<Study> list = studyElsRepository.findByContent(keyword);
        StringBuilder promptBuilder = new StringBuilder();
        for (Study study : list) {
            promptBuilder.append(study.getContent());
        }
        String prompt = promptBuilder.toString() + "에서 " + keyword + "라는 말에 답변을 해줘";

        return gptService.getGPTResponse(new GPTRequestDto(prompt))
                .map(response -> {
                    GPTResponseDto dto = new GPTResponseDto();
                    dto.setResponse(response.getResponse());
                    return dto;
                })
                .doOnError(error -> {
                    // 오류가 발생했을 때 실행되는 코드
                    System.err.println("Error occurred: " + error.getMessage());
                });
    }
}
