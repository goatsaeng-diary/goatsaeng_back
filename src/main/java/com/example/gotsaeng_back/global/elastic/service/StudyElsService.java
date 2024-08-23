package com.example.gotsaeng_back.global.elastic.service;

import static com.example.gotsaeng_back.global.exception.ExceptionEnum.INTERNAL_SERVER_ERROR;

import com.example.gotsaeng_back.global.elastic.index.Study;
import com.example.gotsaeng_back.global.elastic.repository.StudyElsRepository;
import com.example.gotsaeng_back.global.exception.ApiException;
import com.example.gotsaeng_back.global.gptapi.dto.GPTRequestDto;
import com.example.gotsaeng_back.global.gptapi.dto.GPTResponseDto;
import com.example.gotsaeng_back.global.gptapi.service.GPTService;
import com.example.gotsaeng_back.global.response.CustomResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.HttpStatus;
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
                    throw new ApiException(INTERNAL_SERVER_ERROR);
                });
    }

    public CustomResponse<List<String>> findAll() {
        try {
            // ElasticSearch에서 모든 문서 검색
            Iterable<Study> iterable = studyElsRepository.findAll();

            // Iterable<Study>를 List<Study>로 변환
            List<String> list = new ArrayList<>();
            for (Study study : iterable) {
                list.add(study.getTitle());
            }

            // 성공적인 응답 반환
            return new CustomResponse<>(HttpStatus.OK, "문서 목록을 가져왔습니다", list);

        } catch (Exception e) {
            // 예외 발생 시 응답 생성
            throw  new ApiException(INTERNAL_SERVER_ERROR);
        }
    }
}
