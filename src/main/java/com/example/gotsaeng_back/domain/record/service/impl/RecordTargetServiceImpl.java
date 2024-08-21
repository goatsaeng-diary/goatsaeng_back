package com.example.gotsaeng_back.domain.record.service.impl;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.domain.record.dto.request.RecordTargetRequestDto;
import com.example.gotsaeng_back.domain.record.entity.RecordTarget;
import com.example.gotsaeng_back.domain.record.repository.RecordTargetRepository;
import com.example.gotsaeng_back.domain.record.service.RecordTargetService;
import com.example.gotsaeng_back.global.exception.ApiException;
import com.example.gotsaeng_back.global.exception.ExceptionEnum;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordTargetServiceImpl implements RecordTargetService {
    private final RecordTargetRepository recordTargetRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 목표 생성
    @Override
    @Transactional
    public RecordTarget saveRecordTarget(RecordTargetRequestDto dto, String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }
        if (dto.getRecordType() == null && dto.getCustomRecordType() == null) {
            throw new ApiException(ExceptionEnum.RECORD_TARGET_TYPE_NOT_FOUND);
        }
        if (dto.getRecordType() != null && dto.getCustomRecordType() != null) {
            throw new ApiException(ExceptionEnum.RECORD_TARGET_TYPE_DUPLICATE);
        }

        if (dto.getRecordType() != null) {
            if (recordTargetRepository.existsByUserAndRecordTypeAndDate(user, dto.getRecordType(), LocalDate.now())) {
                throw new ApiException(ExceptionEnum.RECORD_TARGET_DUPLICATE);
            }
        }
        if (dto.getCustomRecordType() != null) {
            if (recordTargetRepository.existsByUserAndCustomRecordTypeAndDate(user, dto.getCustomRecordType(), LocalDate.now())) {
                throw new ApiException(ExceptionEnum.RECORD_TARGET_DUPLICATE);
            }
        }

        // 새로운 목표 생성
        RecordTarget recordTarget = RecordTarget.builder()
                .user(user)
                .target(dto.getTarget())
                .date(LocalDate.now())
                .customRecordType(dto.getCustomRecordType())
                .recordType(dto.getRecordType())
                .build();

        return recordTargetRepository.save(recordTarget);
    }

    // 목표 수정
    @Override
    @Transactional
    public RecordTarget updateRecordTarget(Long targetId, RecordTargetRequestDto dto, String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        RecordTarget existingRecordTarget = recordTargetRepository.findById(targetId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.RECORD_TARGET_NOT_FOUND));

        // 기존 목표 수정
        existingRecordTarget.setTarget(dto.getTarget());
        existingRecordTarget.setDate(LocalDate.now());

        return recordTargetRepository.save(existingRecordTarget);
    }

    // 단일 조회 - 기본 기록 타입
    @Override
    @Transactional(readOnly = true)
    public RecordTarget findDefaultTarget(Long recordTypeId, String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);
        return recordTargetRepository.findByUserAndRecordType_RecordTypeId(user, recordTypeId);
    }

    // 단일 조회 - 커스텀 기록 타입
    @Override
    @Transactional(readOnly = true)
    public RecordTarget findCustomTarget(Long customRecordTypeId, String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);
        return recordTargetRepository.findByUserAndCustomRecordType_CustomRecordTypeId(user, customRecordTypeId);
    }

    // 전체 조회 - 기본 기록 타입
    @Override
    @Transactional(readOnly = true)
    public List<RecordTarget> findAllDefaultTargets(Long recordTypeId, String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        return recordTargetRepository.findAllByUserIdAndRecordTypeId(userId, recordTypeId);
    }

    // 전체 조회 - 커스텀 기록 타입
    @Override
    @Transactional(readOnly = true)
    public List<RecordTarget> findAllCustomTargets(Long customRecordTypeId, String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        return recordTargetRepository.findAllByUserIdAndCustomRecordTypeId(userId, customRecordTypeId);
    }
}