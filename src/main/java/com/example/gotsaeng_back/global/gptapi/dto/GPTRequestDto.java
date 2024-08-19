package com.example.gotsaeng_back.global.gptapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GPTRequestDto {
    private String prompt; // 사용자 입력 프롬프트
}