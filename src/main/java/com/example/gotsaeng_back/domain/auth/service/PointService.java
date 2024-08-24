package com.example.gotsaeng_back.domain.auth.service;

import com.example.gotsaeng_back.domain.auth.dto.PointDto;
import com.example.gotsaeng_back.domain.auth.entity.User;

import java.util.List;

public interface PointService {
    void save(PointDto pointDto, String token);
    List<PointDto> findByUser(String token);
}
