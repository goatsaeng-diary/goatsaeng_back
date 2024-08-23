package com.example.gotsaeng_back.domain.auth.controller;

import com.example.gotsaeng_back.domain.auth.dto.PointDto;
import com.example.gotsaeng_back.domain.auth.service.PointService;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point")
public class PointController {
    private final PointService pointService;
    private final UserService userService;

    @PostMapping("/add")
    public void addPoint(@RequestBody PointDto pointDto, @RequestHeader("Authorization") String token){
        pointService.save(pointDto,token);

        userService.updateUser()
    }
}
