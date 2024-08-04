package com.example.gotsaeng_back.post.dto.reply;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ShowReplyDTO {
    private Long replyId;
    private Long commentId;
    private String content;
    private LocalDateTime createdDate;
    private String username;
}
