package com.example.gotsaeng_back.domain.auth.service.impl;

import static com.example.gotsaeng_back.global.exception.ExceptionEnum.ACCESS_DENIED_EXCEPTION;
import static com.example.gotsaeng_back.global.exception.ExceptionEnum.FAIL_EMAIL_SEND;
import static com.example.gotsaeng_back.global.exception.ExceptionEnum.ID_PASSWORD_FAIL;
import static com.example.gotsaeng_back.global.exception.ExceptionEnum.INTERNAL_SERVER_ERROR;

import com.example.gotsaeng_back.domain.auth.dto.SignUpDto;
import com.example.gotsaeng_back.domain.auth.dto.UserUpdateDto;
import com.example.gotsaeng_back.domain.auth.entity.EmailValid;
import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.entity.User.RoleType;
import com.example.gotsaeng_back.domain.auth.oauth2.dto.OAuthAttributes;
import com.example.gotsaeng_back.domain.auth.repository.UserRepository;
import com.example.gotsaeng_back.domain.auth.service.EmailService;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.global.exception.ApiException;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import com.example.gotsaeng_back.global.response.CustomResponse;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JavaMailSender emailSender;
    private final EmailService emailService;



    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
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

    @Transactional
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

    @Transactional
    @Override
    public void deleteUser(Long userId) {

        userRepository.deleteById(userId);
        User user = findById(userId);
        if(user!=null) {
            throw new ApiException(INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
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

    @Transactional
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
            throw new ApiException(ID_PASSWORD_FAIL);
        }
        return user;
    }

    @Transactional
    @Override
    public String sendEmail(String to) throws Exception {
        String ePw = createKey();
        MimeMessage message = createMessage(to,ePw);
        try{
            emailSender.send(message);
            //메일인증테이블 insert
            emailService.saveEmailAndCode(to,ePw);
        }catch(MailException es){
            es.printStackTrace();
            throw new ApiException(FAIL_EMAIL_SEND);
        }
        return ePw;
    }



    private MimeMessage createMessage(String to,String ePw)throws Exception{
        System.out.println("보내는 대상 : "+ to);
        System.out.println("인증 번호 : "+ePw);
        MimeMessage  message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);//보내는 대상
        message.setSubject("goatsaeng 이메일 인증");//제목

        String msgg="";
        msgg+= "<div style='margin:20px;'>";
        msgg+= "<h1> 안녕하세요 goatsaeng. </h1>";
        msgg+= "<br>";
        msgg+= "<p>아래 코드를 복사해 입력해주세요<p>";
        msgg+= "<br>";
        msgg+= "<p>감사합니다.<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+= ePw+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("dkekah3@gmail.com","goatsaeng"));//보내는 사람

        return message;
    }
    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }

    @Transactional
    @Override
    public CustomResponse<?> verifyCode(String email, String code) {
        EmailValid emailValid = emailService.findByEmail(email);
        if(emailValid.getCode().equals(code)){
            emailService.deleteByEmail(email);
            return new CustomResponse<>(HttpStatus.OK,"인증이 완료 되었습니다",null);
        }else{
            return new CustomResponse<>(HttpStatus.OK,"인증에 실패했습니다",null);
        }
    }
}
