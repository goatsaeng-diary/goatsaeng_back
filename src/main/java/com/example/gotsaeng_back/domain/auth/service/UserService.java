package com.example.gotsaeng_back.domain.auth.service;

import com.example.gotsaeng_back.domain.auth.dto.SignUpDto;
import com.example.gotsaeng_back.domain.auth.dto.UserUpdateDto;
import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.oauth2.dto.OAuthAttributes;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserService {

    User findByUsername(String username);
    void regUser(SignUpDto signUpDto);
    List<User> userList();
    User saveOrUpdate(OAuthAttributes attributes);
    User findByEmail(String email);
    void addCookies(HttpServletResponse response, User user);
    void deleteUser(Long userId);
    void deleteCookie(HttpServletResponse response);
    UserUpdateDto updateUser(User user , Long userId);
    User findById(Long userId);
    User userLogin(String username , String password);
}
