package com.example.gotsaeng_back.auth.service.impl;

import com.example.gotsaeng_back.auth.entity.Role;
import com.example.gotsaeng_back.auth.entity.User;
import com.example.gotsaeng_back.auth.repository.UserRepository;
import com.example.gotsaeng_back.auth.service.UserService;
import com.example.gotsaeng_back.jwt.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User regUser(User user){
//        Role userRole = Role.findByAuthority("USER");
//        // 사용자에게 역할 할당
//        Set<Authority> roles = new HashSet<>();
//        roles.add(userRole);
//        user.setAuthorities(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<User> userList() {
        return userRepository.findAll();
    }
}
