package com.example.gotsaeng_back.domain.auth.controller;

import com.example.gotsaeng_back.domain.auth.dto.SideUserInfoDto;
import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.service.MyPageService;
import com.example.gotsaeng_back.global.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    //sidebar에서 조회
    @GetMapping("/semi-user")
    public CustomResponse<SideUserInfoDto> getSemiUserInfo(@RequestHeader("Authorization") String token) {
        User user = myPageService.findInfoByUserId(token);
        SideUserInfoDto sideUserInfoDto = SideUserInfoDto.fromEntity(user);
        return new CustomResponse<>(HttpStatus.OK, "sidebar조회 완료", sideUserInfoDto);
    }
    //마이페이지 조회
}
