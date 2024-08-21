package com.example.gotsaeng_back.domain.auth.dto;

import com.example.gotsaeng_back.domain.auth.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SideUserInfoDto {
    private String nickname;
    private Long totalPoint;
    //뱃지

    public static SideUserInfoDto fromEntity(User user) {
        return SideUserInfoDto.builder()
                .nickname(user.getNickname())
                .totalPoint(user.getTotalPoint())
                .build();
    }
}
