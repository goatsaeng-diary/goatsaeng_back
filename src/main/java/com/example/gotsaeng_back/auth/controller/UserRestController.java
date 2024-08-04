package com.example.gotsaeng_back.auth.controller;

import com.example.gotsaeng_back.auth.dto.TokenDto;
import com.example.gotsaeng_back.auth.entity.User;
import com.example.gotsaeng_back.auth.service.UserService;
import com.example.gotsaeng_back.global.response.controller.ApiResponse;
import com.example.gotsaeng_back.jwt.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ApiResponse<?> login(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                HttpServletResponse response) {
        User user = userService.findByUsername(username);
        if(user==null){
            return new ApiResponse<>(false,"로그인실패");
        }
        if(!passwordEncoder.matches(password, user.getPassword())){
            return new ApiResponse<>(false,"로그인실패");
        }

        String accessToken = jwtUtil.createAccessToken(user.getUserId(),user.getEmail(),user.getUsername(),user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId(),user.getEmail(),user.getUsername(),user.getRole());
        TokenDto tokenDto = new TokenDto(accessToken,refreshToken);
        return new ApiResponse<>(true,"토큰발급",tokenDto);
    }
    @PostMapping("/sign-up")
    public ApiResponse<?> userreg(@RequestBody User user){
        User regUser = userService.regUser(user);
        if (regUser!=null){
            return new ApiResponse<>(true,"회원가입 완료");
        }else{
            return new ApiResponse<>(false,"회원가입 실패");

        }
    }

    @PostMapping("/logout")
    public ApiResponse<?>logout(HttpServletResponse response){
        //프론트랑 연결 후 로그아웃 구현
        return null;
    }

    @DeleteMapping("/withdraw")
    public ApiResponse<?>withdrawUser(@RequestHeader("Authorization") String token){
        Long userId = jwtUtil.getUserIdFromToken(token);
        userService.deleteUser(userId);
        User user = userService.findById(userId);
        if(user!=null){
            return new ApiResponse<>(false,"회원탈퇴실패");
        }else{
            return new ApiResponse<>(true,"회원탈퇴성공");
        }
    }
//    @GetMapping("/userList")
//    public List<User> userList(@RequestHeader("Authorization") String token){
//        System.out.println(jwtUtil.getUserIdFromToken(token)+"123123");
//        return userService.userList();
//    }

    @PostMapping("/update")
    public ApiResponse<?> userUpdate(@RequestBody User user){
        User updateUser = userService.saveOrUpdateUser(user);
        if (updateUser!=null){
            return new ApiResponse<>(true,"수정 완료");
        }else{
            return new ApiResponse<>(false,"수정 실패");

        }
    }
    @GetMapping("/duplicate")
    public ApiResponse<?> userDuplicate(@RequestParam String username){
        User duplicateUser = userService.findByUsername(username);
        if(duplicateUser.getUserId()!=null){
            return new ApiResponse<>(false,"중복된 아이디입니다",duplicateUser.getUsername());
        }else{
            return new ApiResponse<>(true , "사용 가능한 아이디 입니다",duplicateUser.getUsername());
        }
    }
}
