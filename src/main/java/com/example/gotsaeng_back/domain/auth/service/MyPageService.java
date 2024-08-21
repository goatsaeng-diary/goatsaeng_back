package com.example.gotsaeng_back.domain.auth.service;

import com.example.gotsaeng_back.domain.auth.entity.User;

public interface MyPageService {
    User findInfoByUserId(String token);
}
