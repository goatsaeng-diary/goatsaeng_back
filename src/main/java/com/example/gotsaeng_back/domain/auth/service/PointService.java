package com.example.gotsaeng_back.domain.auth.service;

import com.example.gotsaeng_back.domain.auth.dto.PointDto;
import com.example.gotsaeng_back.domain.auth.entity.User;

public interface PointService {
    void save(PointDto pointDto, String token);
}
