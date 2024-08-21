package com.example.gotsaeng_back.global.gptapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GPTResponseDto {
    private String response; // GPT의 응답 메시지
}