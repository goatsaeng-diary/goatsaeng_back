package com.example.gotsaeng_back.domain.auth.controller;

import com.example.gotsaeng_back.domain.auth.dto.PointDto;
import com.example.gotsaeng_back.domain.auth.service.PointService;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.global.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point")
public class PointController {
    private final PointService pointService;

    @GetMapping("/list")
    public CustomResponse<List<PointDto>> getAllPoints(@RequestHeader("Authorization") String token) {
        List<PointDto> list = pointService.findByUser(token);
        return new CustomResponse<>(HttpStatus.OK, "포인트 목록 조회 성공", list);
    }

    @GetMapping("/total")
    public CustomResponse<Long> getTotalPoints(@RequestHeader("Authorization") String token) {
        List<PointDto> list = pointService.findByUser(token);
        Long totalPoint = 0L;
        for(PointDto pointDto : list){
            totalPoint += pointDto.getValue();
        }
        return new CustomResponse<>(HttpStatus.OK, "사용자 전체 포인트 조회 성공", totalPoint);
    }
}
