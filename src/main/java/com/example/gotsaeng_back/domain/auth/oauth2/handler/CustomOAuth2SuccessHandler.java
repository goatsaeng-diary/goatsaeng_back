package com.example.gotsaeng_back.domain.auth.oauth2.handler;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;

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
            response.sendRedirect("/success");
        }
    }
}
