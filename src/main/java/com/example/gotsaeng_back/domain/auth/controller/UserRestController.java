package com.example.gotsaeng_back.domain.auth.controller;

import com.example.gotsaeng_back.domain.auth.dto.TokenDto;
import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.jwt.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        String accessToken = jwtUtil.createAccessToken(user.getUserId(),user.getEmail(),user.getUsername(),user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId(),user.getEmail(),user.getUsername(),user.getRole());

        return ResponseEntity.ok(new TokenDto(accessToken, refreshToken));
    }
    @PostMapping("/userreg")
    public ResponseEntity<?> userreg(@RequestBody User user){
        User regUser = userService.regUser(user);
        if (regUser!=null){
            return ResponseEntity.ok("회원가입 완료");
        }else{
            return ResponseEntity.ok("회원가입 실패");
        }
    }

    @GetMapping("/userList")
    public List<User> userList(){
        return userService.userList();
    }
}
