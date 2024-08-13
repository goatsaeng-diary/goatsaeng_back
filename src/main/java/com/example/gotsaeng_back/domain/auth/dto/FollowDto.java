package com.example.gotsaeng_back.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowDto {
    String name;
    String userImage;
    Long userId;
}
