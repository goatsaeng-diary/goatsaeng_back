package com.example.gotsaeng_back.global.jwt.service;


import com.example.gotsaeng_back.auth.entity.User;
import com.example.gotsaeng_back.auth.service.UserService;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("사용자가 없습니다.");
        }

        Hibernate.initialize(user.getRole());

        // role이 null인지 확인하고 기본값 또는 예외 처리
        String role = user.getRole() != null ? String.valueOf(user.getRole()) : "USER"; // 기본값 설정

        // 비밀번호가 null인지 확인
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("비밀번호가 설정되어 있지 않습니다.");
        }

        org.springframework.security.core.userdetails.User.UserBuilder userBuilder =
                org.springframework.security.core.userdetails.User.withUsername(username)
                        .password(user.getPassword()) // 비밀번호 설정
                        .roles(role); // 역할 설정

        return userBuilder.build();
    }
}
