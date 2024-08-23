package com.example.gotsaeng_back.global.gptapi.controller;

import java.io.IOException;

import com.example.gotsaeng_back.global.gptapi.dto.response.ChatGPTResponse;
import com.example.gotsaeng_back.global.gptapi.service.impl.AiCallService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OpenAiAPIController {
    private final AiCallService aiCallService;

    @PostMapping("/image")
    public String imageAnalysis(@RequestParam MultipartFile image)
            throws IOException {
        ChatGPTResponse response = aiCallService.requestImageAnalysis(image, "이 사진은 무엇을 인증하고 있나요? 운동을 인증하는거 같으면 '운동', 공부를 인증하는거 같으면 '공부', 아무것도 아닌거같으면 'none'이라는 대답만 보내줘.");
        return response.getChoices().getFirst().getMessage().getContent();
    }

    @PostMapping("/text")
    public String textAnalysis(@RequestParam String requestText) {
        ChatGPTResponse response = aiCallService.requestTextAnalysis(requestText);
        return response.getChoices().getFirst().getMessage().getContent();
    }
}
