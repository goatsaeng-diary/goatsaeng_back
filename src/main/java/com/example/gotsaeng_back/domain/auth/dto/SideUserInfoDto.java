package com.example.gotsaeng_back.domain.auth.dto;

import com.example.gotsaeng_back.domain.auth.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SideUserInfoDto {
    private String imageUrl;
    private String nickname;
    private Long totalPoint;
}
