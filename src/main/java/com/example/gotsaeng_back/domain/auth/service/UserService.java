package com.example.gotsaeng_back.domain.auth.service;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.oauth2.dto.OAuthAttributes;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserService {

    User findByUsername(String username);
    User regUser(User user);
    List<User> userList();
    User saveOrUpdate(OAuthAttributes attributes);
    User findByEmail(String email);
    void addCookies(HttpServletResponse response, User user);
    void deleteUser(Long userId);
    void deleteCookie(HttpServletResponse response);
    User saveOrUpdateUser(User user);
    User findById(Long userId);
}
