package com.example.gotsaeng_back.global.gptapi.service;

import com.example.gotsaeng_back.global.gptapi.dto.GPTRequestDto;
import com.example.gotsaeng_back.global.gptapi.dto.GPTResponseDto;
import com.example.gotsaeng_back.global.gptapi.entity.Word;
import reactor.core.publisher.Mono;

public interface GPTService {
    // GPT API 호출을 처리하는 메서드
    Mono<GPTResponseDto> getGPTResponse(GPTRequestDto requestDto, Word word);
    Mono<GPTResponseDto> getGPTResponse(GPTRequestDto requestDto);

    String getQuestion(Word word);

    Word getRandomWord();
}
