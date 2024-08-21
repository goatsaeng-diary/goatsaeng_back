package com.example.gotsaeng_back.domain.record.service.impl;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.domain.record.dto.request.CustomRecordTypeRequestDto;
import com.example.gotsaeng_back.domain.record.entity.CustomRecordType;
import com.example.gotsaeng_back.domain.record.repository.CustomRecordTypeRepository;
import com.example.gotsaeng_back.domain.record.service.CustomRecordTypeService;
import com.example.gotsaeng_back.global.exception.ApiException;
import com.example.gotsaeng_back.global.exception.ExceptionEnum;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomRecordTypeImpl implements CustomRecordTypeService {
    private final CustomRecordTypeRepository customRecordTypeRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    //생성
    @Override
    @Transactional
    public CustomRecordType saveCustomRecordType(String token, CustomRecordTypeRequestDto dto) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        CustomRecordType existingCustomRecordType = customRecordTypeRepository.findByUserAndTypeName(user, dto.getTypeName());

        if (existingCustomRecordType != null) {
            throw new ApiException(ExceptionEnum.CUSTOM_RECORD_TYPE_DUPLICATE);
        } else {
            CustomRecordType customRecordType = CustomRecordType.builder()
                    .user(user)
                    .typeName(dto.getTypeName())
                    .field(dto.getField())
                    .unit(dto.getUnit())
                    .build();
            return customRecordTypeRepository.save(customRecordType);
        }
    }

    //수정
    @Override
    @Transactional
    public CustomRecordType updateCustomRecordType(Long customRecordTypeId, String token, CustomRecordTypeRequestDto dto) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        CustomRecordType customRecordType = findByCustomRecordTypeId(customRecordTypeId);

        if(!user.getUserId().equals(customRecordType.getUser().getUserId())) {
            throw new ApiException(ExceptionEnum.CUSTOM_RECORD_TYPE_DELETE_FORBIDDEN);
        }

        customRecordType.setField(dto.getField());
        customRecordType.setUnit(dto.getUnit());
        return customRecordTypeRepository.save(customRecordType);
    }

    //삭제
    @Override
    @Transactional
    public void deleteCustomRecordType(Long customRecordTypeId, String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        CustomRecordType customRecordType = findByCustomRecordTypeId(customRecordTypeId);

        if(!user.getUserId().equals(customRecordType.getUser().getUserId())) {
            throw new ApiException(ExceptionEnum.CUSTOM_RECORD_TYPE_DELETE_FORBIDDEN);
        }
        customRecordTypeRepository.deleteById(customRecordTypeId);
    }

    //단일 조회
    @Override
    @Transactional
    public CustomRecordType findByCustomRecordTypeId(Long customRecordTypeId) {
        return customRecordTypeRepository.findById(customRecordTypeId).orElseThrow(() -> new ApiException(ExceptionEnum.CUSTOM_RECORD_TYPE_NOT_FOUND));
    }

    //전체 조회 -- user에 대한 모든 것
    @Override
    @Transactional
    public List<CustomRecordType> showAllCustomRecordType(String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        return customRecordTypeRepository.findAllByUser(user);
    }

}
