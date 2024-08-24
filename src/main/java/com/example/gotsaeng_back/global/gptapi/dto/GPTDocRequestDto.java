package com.example.gotsaeng_back.global.gptapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GPTDocRequestDto {
    private String prompt; // 사용자 입력 프롬프트
    List<String> titleList = new ArrayList<>();
}