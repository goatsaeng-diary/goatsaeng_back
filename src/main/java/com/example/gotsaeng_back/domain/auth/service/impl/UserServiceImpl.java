package com.example.gotsaeng_back.domain.auth.service.impl;

import static com.example.gotsaeng_back.global.exception.ExceptionEnum.ACCESS_DENIED_EXCEPTION;
import static com.example.gotsaeng_back.global.exception.ExceptionEnum.INTERNAL_SERVER_ERROR;

import com.example.gotsaeng_back.domain.auth.dto.SignUpDto;
import com.example.gotsaeng_back.domain.auth.dto.UserUpdateDto;
import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.entity.User.RoleType;
import com.example.gotsaeng_back.domain.auth.oauth2.dto.OAuthAttributes;
import com.example.gotsaeng_back.domain.auth.repository.UserRepository;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.global.exception.ApiException;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    public void regUser(SignUpDto dto){
        User user = new User();
        try{
            user.setRole(RoleType.USER);
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setRegistrationDate(LocalDateTime.now());
            user.setEmail(dto.getEmail());
            user.setUsername(dto.getUsername());
            user.setBirthDate(dto.getBirthDate());
            user.setName(dto.getName());
            user.setTotalPoint(0L);
            user.setNickname(dto.getNickname());
        }catch (Exception e){
            throw new ApiException(INTERNAL_SERVER_ERROR);
        }
        userRepository.save(user);
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
        User user = findById(userId);
        if(user!=null) {
            throw new ApiException(INTERNAL_SERVER_ERROR);
        }
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
    public UserUpdateDto updateUser(User user , Long userId) {
        UserUpdateDto dto = new UserUpdateDto();
        try{

            User currentUser = findById(userId);
            currentUser.setUsername(user.getUsername());
            currentUser.setName(user.getName());
            currentUser.setBirthDate(user.getBirthDate());
            currentUser.setEmail(user.getEmail());

            User updateUser = userRepository.save(currentUser);
            dto.setUserId(updateUser.getUserId());
            dto.setUsername(updateUser.getUsername());
            dto.setEmail(updateUser.getEmail());
            dto.setBirthDate(updateUser.getBirthDate());
        }catch(Exception e){
            throw new ApiException(INTERNAL_SERVER_ERROR);
        }

        return dto;

    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User userLogin(String username, String password) {
        User user = findByUsername(username);
        if(user==null){
            throw new ApiException(ACCESS_DENIED_EXCEPTION);
        }
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new ApiException(ACCESS_DENIED_EXCEPTION);
        }
        return user;
    }

}
