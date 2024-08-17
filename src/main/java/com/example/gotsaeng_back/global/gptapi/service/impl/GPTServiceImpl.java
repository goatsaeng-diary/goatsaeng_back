package com.example.gotsaeng_back.global.gptapi.service.impl;

import com.example.gotsaeng_back.global.gptapi.WordRepository;
import com.example.gotsaeng_back.global.gptapi.dto.GPTRequestDto;
import com.example.gotsaeng_back.global.gptapi.dto.GPTResponseDto;
import com.example.gotsaeng_back.global.gptapi.entity.Word;
import com.example.gotsaeng_back.global.gptapi.service.GPTService;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class GPTServiceImpl implements GPTService {

    @Autowired
    private WebClient webClient; // 설정된 WebClient를 주입받음

    @Autowired
    private WordRepository wordRepository;

    @Override
    public Mono<GPTResponseDto> getGPTResponse(GPTRequestDto requestDto, String userAccessToken) {
        // WebClient를 사용하여 OpenAI API에 요청을 보냄
        return webClient.post()
                .uri("/chat/completions")  // OpenAI API 엔드포인트
//                .header("Authorization", "Bearer " + userAccessToken)  // 사용자 인증 토큰 추가
                .bodyValue(Map.of(
                        "model", "gpt-3.5-turbo",  // 사용할 GPT 모델
                        "messages", new Object[]{ // 대화의 내용을 전달
                                Map.of(
                                        "role", "user",
                                        "content", requestDto.getPrompt()  // 사용자 프롬프트 추가
                                )
                        }
                ))
                .retrieve()  // API 응답을 받아옴
                .bodyToMono(Map.class)  // 응답을 Mono 객체로 변환
                .map(response -> {
                    // 응답에서 필요한 메시지를 추출하여 DTO로 변환
                    Map<String, Object> choices = (Map<String, Object>) ((java.util.List<?>) response.get("choices")).get(0);
                    Map<String, String> message = (Map<String, String>) choices.get("message");
                    return new GPTResponseDto(message.get("content"));
                });
    }

    @Override
    public String getQuestion() {
        Long count = wordRepository.countWords();
        Random random = new Random();
        Long randomNumber = random.nextLong(count) + 1;
        Word word = wordRepository.findById(randomNumber).orElseThrow();
        System.out.println(word.getWordName());
        return word.getWordName()+"From now on, your answers to my questions will be in Korean."
                + "Could you give me an easy problem related to this word? The problem format is as follows"
                + "Problem/Correct Answer"
                + "We will separate the questions and answers by separating them with /. ";
    }
}
