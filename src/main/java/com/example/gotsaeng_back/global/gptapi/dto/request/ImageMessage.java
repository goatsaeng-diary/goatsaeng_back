package com.example.gotsaeng_back.global.gptapi.dto.request;

import java.util.List;

import com.example.gotsaeng_back.global.gptapi.dto.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ImageMessage extends Message {
    private List<Content> content;

    public ImageMessage(String role, List<Content> content) {
        super(role);
        this.content = content;
    }
}
