package com.example.gotsaeng_back.global.gptapi.dto.response;

import com.example.gotsaeng_back.global.gptapi.dto.TextMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Choice {
    private int index;
    private TextMessage message;
}
