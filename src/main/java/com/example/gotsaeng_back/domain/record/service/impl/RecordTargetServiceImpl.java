package com.example.gotsaeng_back.domain.record.service.impl;//package com.example.gotsaeng_back.domain.record.service.impl;
//
//import com.example.gotsaeng_back.domain.auth.entity.User;
//import com.example.gotsaeng_back.domain.auth.service.UserService;
//import com.example.gotsaeng_back.domain.record.dto.request.RecordTargetRequestDto;
//import com.example.gotsaeng_back.domain.record.entity.RecordTarget;
//import com.example.gotsaeng_back.domain.record.entity.RecordType;
//import com.example.gotsaeng_back.domain.record.repository.RecordTargetRepository;
//import com.example.gotsaeng_back.global.exception.ApiException;
//import com.example.gotsaeng_back.global.exception.ExceptionEnum;
//import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class RecordTargetServiceImpl {
//    private final RecordTargetRepository recordTargetRepository;
//    private final UserService userService;
//    private final JwtUtil jwtUtil;
//
//    //생성
//    @Transactional
//    public RecordTarget saveRecordTarget(RecordTargetRequestDto dto, String token) {
//        Long userId = jwtUtil.getUserIdFromToken(token);
//        User user = userService.findById(userId);
//
//        if (user == null) {
//            throw new ApiException(ExceptionEnum.BAD_REQUEST);
//        }
//    }
//
//    //수정
//    @Transactional
//
//    //단일 조회
//    @Transactional(readOnly = true)
//}
