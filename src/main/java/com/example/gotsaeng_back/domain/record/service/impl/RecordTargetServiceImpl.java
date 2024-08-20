package com.example.gotsaeng_back.domain.record.service.impl;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.domain.record.dto.request.RecordTargetRequestDto;
import com.example.gotsaeng_back.domain.record.entity.CustomRecordType;
import com.example.gotsaeng_back.domain.record.entity.RecordTarget;
import com.example.gotsaeng_back.domain.record.entity.RecordType;
import com.example.gotsaeng_back.domain.record.repository.RecordTargetRepository;
import com.example.gotsaeng_back.domain.record.service.RecordTargetService;
import com.example.gotsaeng_back.domain.record.service.RecordTypeService;
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

    //생성
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

        RecordTarget recordTarget = RecordTarget.builder()
                .user(user)
                .target(dto.getTarget())
                .date(LocalDate.now())
                .customRecordType(dto.getCustomRecordType())
                .recordType(dto.getRecordType())
                .build();

        return recordTargetRepository.save(recordTarget);
    }

    //수정
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

        if (dto.getRecordType() == null && dto.getCustomRecordType() == null) {
            throw new ApiException(ExceptionEnum.RECORD_TARGET_TYPE_NOT_FOUND);
        }
        if (dto.getRecordType() != null && dto.getCustomRecordType() != null) {
            throw new ApiException(ExceptionEnum.RECORD_TARGET_TYPE_DUPLICATE);
        }

        // 기존 레코드 수정
        existingRecordTarget.setTarget(dto.getTarget());
        existingRecordTarget.setDate(LocalDate.now());
        existingRecordTarget.setCustomRecordType(dto.getCustomRecordType());
        existingRecordTarget.setRecordType(dto.getRecordType());

        return recordTargetRepository.save(existingRecordTarget);
    }

    // 단일 조회 - 기본
    @Override
    @Transactional(readOnly = true)
    public RecordTarget findDefaultTarget(Long recordTypeId, String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        return recordTargetRepository.findByUserIdAndRecordTypeId(userId, recordTypeId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.RECORD_TARGET_NOT_FOUND));
    }

    // 단일 조회 - 커스텀
    @Override
    @Transactional(readOnly = true)
    public RecordTarget findCustomTarget(Long customRecordTypeId, String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        return recordTargetRepository.findByUserIdAndCustomRecordTypeId(userId, customRecordTypeId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.RECORD_TARGET_NOT_FOUND));
    }

    // 전체 조회 - 기본
    @Override
    @Transactional(readOnly = true)
    public List<RecordTarget> findAllDefaultTargets(Long recordTypeId, String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        return recordTargetRepository.findAllByUserIdAndRecordTypeId(userId, recordTypeId);
    }

    // 전체 조회 - 커스텀
    @Override
    @Transactional(readOnly = true)
    public List<RecordTarget> findAllCustomTargets(Long customRecordTypeId, String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        return recordTargetRepository.findAllByUserIdAndCustomRecordTypeId(userId, customRecordTypeId);
    }
}
