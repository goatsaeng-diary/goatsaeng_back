package com.example.gotsaeng_back.auth.service.impl;

import com.example.gotsaeng_back.auth.entity.User;
import com.example.gotsaeng_back.auth.entity.User.RoleType;
import com.example.gotsaeng_back.auth.oauth2.dto.OAuthAttributes;
import com.example.gotsaeng_back.auth.repository.UserRepository;
import com.example.gotsaeng_back.auth.service.UserService;
import com.example.gotsaeng_back.jwt.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


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

    @Override
    public User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail()).orElse(new User());
        user.setUsername(attributes.getName());
        user.setProvider(attributes.getProvider());
        user.setRegistrationDate(LocalDateTime.now());
        user.setEmail(attributes.getEmail());
        user.setBirthDate(LocalDate.now());

        user.setRole(RoleType.USER);
        if(attributes.getAttributes().get("kakao_account")!=null){
            Map<String, Object> properties = (Map<String, Object>) attributes.getAttributes().get("properties");
            String nickname = (String) properties.get("nickname");
            user.setName(nickname);
        }else{
            user.setName((String)attributes.getAttributes().get("name"));
        }
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(new User());

    }

    @Override
    public void addCookies(HttpServletResponse response, User user) {
//        String accessToken = jwtUtil.createAccessToken(
//                user.getUserId(),
//                user.getEmail(),
//                user.getUsername(),
//                user.getRole()
//        );
//        String refreshToken = jwtUtil.createRefreshToken(
//                user.getUserId(),
//                user.getEmail(),
//                user.getUsername(),
//                user.getRole()
//        );
//        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
//        accessTokenCookie.setHttpOnly(true);
//        accessTokenCookie.setPath("/");
//        accessTokenCookie.setMaxAge(Math.toIntExact(jwtUtil.ACCESS_TOKEN_EXPIRE_COUNT / 1000));
//
//        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
//        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setPath("/");
//        refreshTokenCookie.setMaxAge(Math.toIntExact(jwtUtil.REFRESH_TOKEN_EXPIRE_COUNT / 1000));
//
//
//        response.addCookie(accessTokenCookie);
//        response.addCookie(refreshTokenCookie);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void deleteCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("username" , null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        Cookie accessToken = new Cookie("accessToken" , null);
        accessToken.setMaxAge(0);
        accessToken.setPath("/");

        Cookie refreshToken = new Cookie("refreshToken",null);
        refreshToken.setMaxAge(0);
        refreshToken.setPath("/");

        response.addCookie(refreshToken);
        response.addCookie(cookie);
        response.addCookie(accessToken);
    }

    @Override
    public User saveOrUpdateUser(User user) {
        return userRepository.save(user);

    }
}
