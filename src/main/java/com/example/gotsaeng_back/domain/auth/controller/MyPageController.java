package com.example.gotsaeng_back.domain.auth.controller;

import com.example.gotsaeng_back.domain.auth.dto.PointDto;
import com.example.gotsaeng_back.domain.auth.dto.SideUserInfoDto;
import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.service.MyPageService;
import com.example.gotsaeng_back.domain.auth.service.PointService;
import com.example.gotsaeng_back.global.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;
    private final PointService pointService;

    //sidebar에서 조회
    @GetMapping("/semi-user")
    public CustomResponse<SideUserInfoDto> getSemiUserInfo(@RequestHeader("Authorization") String token) {
        User user = myPageService.findInfoByUserId(token);

        List<PointDto> list = pointService.findByUser(token);
        Long totalPoint = 0L;
        for(PointDto pointDto : list){
            totalPoint += pointDto.getValue();
        }

        SideUserInfoDto sideUserInfoDto = new SideUserInfoDto();
        sideUserInfoDto.setNickname(user.getNickname());
        sideUserInfoDto.setImageUrl(user.getUserImage());
        sideUserInfoDto.setTotalPoint(totalPoint);

        return new CustomResponse<>(HttpStatus.OK, "sidebar조회 완료", sideUserInfoDto);
    }
    //마이페이지 조회
}
