package com.example.gotsaeng_back.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowDto {
    String nickname;
    String userImage;
    Long userId;
}
