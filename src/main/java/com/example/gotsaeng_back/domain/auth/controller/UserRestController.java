package com.example.gotsaeng_back.domain.auth.controller;


import static com.example.gotsaeng_back.global.exception.ExceptionEnum.DUPLICATE;

import com.example.gotsaeng_back.domain.auth.dto.LoginDto;
import com.example.gotsaeng_back.domain.auth.dto.SignUpDto;
import com.example.gotsaeng_back.domain.auth.dto.TokenDto;
import com.example.gotsaeng_back.domain.auth.dto.UserUpdateDto;
import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.global.exception.ApiException;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import com.example.gotsaeng_back.global.response.CustomResponse;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public CustomResponse<TokenDto> login(@RequestBody LoginDto loginDto) {
        User user = userService.userLogin(loginDto.getUsername(),loginDto.getPassword());
        String accessToken = jwtUtil.createAccessToken(user.getUserId(),user.getEmail(),user.getUsername(),user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId(),user.getEmail(),user.getUsername(),user.getRole());
        TokenDto tokenDto = new TokenDto(accessToken,refreshToken);
        return new CustomResponse<>(HttpStatus.OK,"환영합니다",tokenDto);
    }
    @PostMapping("/sign-up")
    public CustomResponse<Void> userreg(@RequestBody SignUpDto signUpDto){
        userService.regUser(signUpDto);
        return new CustomResponse<>(HttpStatus.OK,"회원가입이 완료되었습니다.",null);
    }


    @DeleteMapping("/withdraw")
    public CustomResponse<Void> withdrawUser(@RequestHeader("Authorization") String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        userService.deleteUser(userId);
        return new CustomResponse<>(HttpStatus.OK,"success",null);
    }

//    @PostMapping("/update")
//    public CustomResponse<UserUpdateDto> userUpdate(@RequestBody User user,@RequestHeader("Authorization") String token){
//        Long userId = jwtUtil.getUserIdFromToken(token);
//        UserUpdateDto updateUser = userService.updateUser(user,userId);
//        return new CustomResponse<>(HttpStatus.OK,"success",updateUser);
//    }
    @GetMapping("/check-username/{username}")
    public CustomResponse<?> userDuplicate(@PathVariable("username") String username){
        User duplicateUser = userService.findByUsername(username);
        System.out.println(username);
        System.out.println(duplicateUser);
        if(duplicateUser!=null){
            throw new ApiException(DUPLICATE);
        }else{
            System.out.println("사용가능");
            return new CustomResponse<>(HttpStatus.OK,"사용 가능한 아이디 입니다.",username);
        }
    }
}
