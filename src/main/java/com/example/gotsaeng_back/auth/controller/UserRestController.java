package com.example.gotsaeng_back.auth.controller;

import com.example.gotsaeng_back.auth.dto.TokenDto;
import com.example.gotsaeng_back.auth.entity.User;
import com.example.gotsaeng_back.auth.service.UserService;
import com.example.gotsaeng_back.jwt.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   HttpServletResponse response) {
        User user = userService.findByUsername(username);
        if(user==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(!passwordEncoder.matches(password, user.getPassword())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String accessToken = jwtUtil.generateAccessToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);

        //토큰을 쿠키에 저장
        userService.addCookies(response,accessToken,refreshToken,user);

        return ResponseEntity.ok(new TokenDto(accessToken, refreshToken));
    }
    @PostMapping("/userreg")
    public User userreg(@RequestBody User user){
        return userService.regUser(user);
    }
}
