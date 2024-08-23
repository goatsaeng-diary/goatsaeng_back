package com.example.gotsaeng_back.domain.auth.service.impl;

import com.example.gotsaeng_back.domain.auth.dto.PointDto;
import com.example.gotsaeng_back.domain.auth.entity.Point;
import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.repository.PointRepository;
import com.example.gotsaeng_back.domain.auth.service.PointService;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PointRepository pointRepository;

    @Override
    public void save(PointDto pointDto, String token) {
        String username = jwtUtil.getUserNameFromToken(token);
        User user = userService.findByUsername(username);

        Point point = new Point();
        point.setPointHistory("학습하기");
        point.setValue(pointDto.getValue());
        point.setGetDate(pointDto.getGetDate());
        point.setUser(user);

        pointRepository.save(point);
    }
}
