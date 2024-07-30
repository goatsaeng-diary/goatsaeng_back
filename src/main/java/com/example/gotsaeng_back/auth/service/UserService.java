package com.example.gotsaeng_back.auth.service;

import com.example.gotsaeng_back.auth.entity.User;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    User findByUsername(String username);
    void addCookies(HttpServletResponse response, String accessToken, String refreshToken, User user);
    User regUser(User user);
}
