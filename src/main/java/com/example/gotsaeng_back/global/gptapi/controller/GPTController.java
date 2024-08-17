package com.example.gotsaeng_back.global.gptapi.controller;

import com.example.gotsaeng_back.global.gptapi.dto.GPTRequestDto;
import com.example.gotsaeng_back.global.gptapi.dto.GPTResponseDto;
import com.example.gotsaeng_back.global.gptapi.service.GPTService;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class GPTController {

    private final GPTService gptService;
    private final JwtUtil jwtUtil;

    // GPT API 호출을 처리하는 엔드포인트
    @PostMapping("/gpt")
    public Mono<GPTResponseDto> getGptResponse(
            @RequestBody String prompt, // 사용자로부터 입력받는 프롬프트
            @RequestHeader("Authorization") String userAccessToken // 클라이언트에서 전달된 사용자 인증 토큰
    ) {
        String username = jwtUtil.getUserNameFromToken(userAccessToken);
        System.out.println(username);
        // GPT 요청을 위한 DTO 생성
        GPTRequestDto requestDto = new GPTRequestDto(prompt);
        // 서비스 호출하여 GPT 응답을 받음
        return gptService.getGPTResponse(requestDto, userAccessToken);
    }
}
