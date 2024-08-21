package com.example.gotsaeng_back.domain.auth.oauth2.handler;

import static com.example.gotsaeng_back.global.exception.ExceptionEnum.SOCIAL_LOGIN_FAIL;
import static com.example.gotsaeng_back.global.jwt.util.JwtUtil.ACCESS_TOKEN_EXPIRE_COUNT;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.global.exception.ApiException;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
// 인증된 사용자 정보를 가져옴

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user =null;
        if((Map<String, Object>) oAuth2User.getAttributes().get("kakao_account")!=null){
            Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            String email = (String) kakaoAccount.get("email");
            user = userService.findByEmail(email);

        }else{
            user = userService.findByEmail((String)oAuth2User.getAttributes().get("email"));
        }
//        userService.addCookies(response,user);
        if(user.getProvider()!=null){
            try{
                String frontendUrl = "http://localhost:3000"; // 프론트엔드 URL
                String token = jwtUtil.createAccessToken(user.getUserId(),user.getEmail(),user.getUsername(),user.getRole());
                String redirectUrl = frontendUrl + "/oauth2/redirect?token=" + token;
                getRedirectStrategy().sendRedirect(request,response,redirectUrl);
            }catch(Exception e){
                throw new ApiException(SOCIAL_LOGIN_FAIL);
            }
        }
    }
}
