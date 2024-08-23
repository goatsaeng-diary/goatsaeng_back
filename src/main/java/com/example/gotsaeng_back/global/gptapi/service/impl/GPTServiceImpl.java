package com.example.gotsaeng_back.global.gptapi.service.impl;

import com.example.gotsaeng_back.domain.study.service.StudyService;
import com.example.gotsaeng_back.global.gptapi.WordRepository;
import com.example.gotsaeng_back.global.gptapi.dto.GPTRequestDto;
import com.example.gotsaeng_back.global.gptapi.dto.GPTResponseDto;
import com.example.gotsaeng_back.global.gptapi.entity.Word;
import com.example.gotsaeng_back.global.gptapi.service.GPTService;
import java.util.Random;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GPTServiceImpl implements GPTService {

    private final WebClient webClient;
    private final WordRepository wordRepository;
    private final StudyService studyService;

    @Override
    public Mono<GPTResponseDto> getGPTResponse(GPTRequestDto requestDto, Word word) {
        // WebClient를 사용하여 OpenAI API에 요청을 보냄
        return webClient.post()
                .uri("/chat/completions")  // OpenAI API 엔드포인트
//                .header("Authorization", "Bearer " + userAccessToken)  // 사용자 인증 토큰 추가
                .bodyValue(Map.of(
                        "model", "gpt-4o",  // 사용할 GPT 모델
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
                    studyService.save(new GPTResponseDto(message.get("content")), word.getWordName());
                    System.out.println("21312"+word.getWordName());
                    return new GPTResponseDto(message.get("content"));
                });
    }

    @Override
    public String getQuestion(Word word) {
        System.out.println(word.getWordName());
        return word.getWordName()+"이라는 단어로 문제를 내줘. 단어의 정의가 들어가 있어야되고 , 사용자가 "+word.getWordName()+" 이라는 단어를 말했을때 정답이 되도록 해야해. 다음은 너의 대답에 대한 포멧이야. 다른말 하지말고 내가 말해준 단어, 너가 말해야되는 포멧에 맞춰서 대답해. 정신차리고 대답해\n"
                + "문제 : (여기에 문제를 내줘)";
    }

    @Override
    public Word getRandomWord() {
        Long count = wordRepository.countWords();
        Random random = new Random();
        Long randomNumber = random.nextLong(count) + 1;
        Word word = wordRepository.findById(randomNumber).orElseThrow();
        return word;
    }
}
