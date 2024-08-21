package com.example.gotsaeng_back.domain.record.service.impl;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.domain.record.dto.request.CustomRecordRequestDto;
import com.example.gotsaeng_back.domain.record.dto.response.MonthlyRecordResponseDto;
import com.example.gotsaeng_back.domain.record.dto.response.CustomRecordResponseDto;
import com.example.gotsaeng_back.domain.record.entity.CustomRecord;
import com.example.gotsaeng_back.domain.record.entity.CustomRecordType;
import com.example.gotsaeng_back.domain.record.repository.CustomRecordRepository;
import com.example.gotsaeng_back.domain.record.service.CustomRecordService;
import com.example.gotsaeng_back.domain.record.service.CustomRecordTypeService;
import com.example.gotsaeng_back.global.exception.ApiException;
import com.example.gotsaeng_back.global.exception.ExceptionEnum;
import com.example.gotsaeng_back.global.jwt.util.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomRecordServiceImpl implements CustomRecordService {
    private final CustomRecordRepository customRecordRepository;
    private final CustomRecordTypeService customRecordTypeService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 생성
    @Override
    @Transactional
    public CustomRecord saveCustomRecord(CustomRecordRequestDto dto, String token, Long customRecordTypeId) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);
        CustomRecordType customRecordType = customRecordTypeService.findByCustomRecordTypeId(customRecordTypeId);

        if (user == null || customRecordType == null) {
            throw new ApiException(ExceptionEnum.BAD_REQUEST);
        }

        // 날짜 같으면 중복 등록이 아닌 수정으로 처리
        CustomRecord existingRecord = customRecordRepository.findByUserAndCustomRecordTypeAndDate(user, customRecordType, LocalDate.now());

        if (existingRecord != null) {
            existingRecord.setValue(dto.getValue());
            return customRecordRepository.save(existingRecord);
        } else {
            // 기존 기록이 없으면 새로운 기록 생성
            CustomRecord customRecord = CustomRecord.builder()
                    .user(user)
                    .customRecordType(customRecordType)
                    .date(LocalDate.now())
                    .value(dto.getValue())
                    .build();

            return customRecordRepository.save(customRecord);
        }
    }

    // 수정
    @Override
    @Transactional
    public CustomRecord updateCustomRecord(CustomRecordRequestDto dto, String token, Long customRecordId) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        CustomRecord customRecord = findByCustomRecordId(customRecordId);

        if (!user.getUserId().equals(customRecord.getUser().getUserId())) {
            throw new ApiException(ExceptionEnum.RECORD_UPDATE_FORBIDDEN);
        }

        customRecord.setValue(dto.getValue());
        return customRecordRepository.save(customRecord);
    }

    // 삭제
    @Override
    @Transactional
    public void deleteCustomRecord(String token, Long customRecordId) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        CustomRecord customRecord = findByCustomRecordId(customRecordId);

        if (!user.getUserId().equals(customRecord.getUser().getUserId())) {
            throw new ApiException(ExceptionEnum.RECORD_DELETE_FORBIDDEN);
        }

        customRecordRepository.deleteById(customRecordId);
    }

    // 단일 조회
    @Override
    @Transactional(readOnly = true)
    public CustomRecord findByCustomRecordId(Long customRecordId) {
        return customRecordRepository.findById(customRecordId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.RECORD_NOT_FOUND));
    }

    // 단일 조회 - username
    @Override
    @Transactional(readOnly = true)
    public List<CustomRecord> findByCustomRecordUser(String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }
        return customRecordRepository.findByUserAndDate(user, LocalDate.now());
    }

    // 당월 조회
    @Override
    @Transactional(readOnly = true)
    public Page<CustomRecordResponseDto> getCustomRecordForCurrentMonth(String token, Pageable pageable) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        LocalDate start = LocalDate.now().withDayOfMonth(1); // 시작 일
        LocalDate end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()); // 마지막 일

        Page<CustomRecord> customRecordPage = customRecordRepository.findAllByUserAndDateBetween(user, start, end, pageable);

        return customRecordPage.map(customRecord -> CustomRecordResponseDto.builder()
                .customRecordId(customRecord.getCustomRecordId())
                .customRecordTypeId(customRecord.getCustomRecordType().getCustomRecordTypeId())
                .date(customRecord.getDate())
                .value(customRecord.getValue())
                .build());
    }

    // 전체 기록을 월별로 조회 (평균치 계산)
    @Override
    @Transactional(readOnly = true)
    public Page<MonthlyRecordResponseDto> getCustomRecordsByMonth(String token, Long customRecordTypeId, Pageable pageable) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        List<CustomRecord> customRecordList = customRecordRepository.findAllByUser(user);

        // 주어진 customRecordTypeId로 필터링
        List<CustomRecord> filteredRecords = customRecordList.stream()
                .filter(customRecord -> customRecord.getCustomRecordType().getCustomRecordTypeId().equals(customRecordTypeId))
                .collect(Collectors.toList());

        // 기록을 월별로 그룹화
        Map<LocalDate, List<CustomRecord>> recordsByMonth = filteredRecords.stream()
                .collect(Collectors.groupingBy(customRecord -> LocalDate.of(customRecord.getDate().getYear(), customRecord.getDate().getMonth(), 1)));

        List<MonthlyRecordResponseDto> summary = recordsByMonth.entrySet().stream()
                .map(monthEntry -> {
                    LocalDate month = monthEntry.getKey();
                    List<CustomRecord> monthlyRecords = monthEntry.getValue();

                    // 참여 횟수
                    long participationCount = monthlyRecords.size();

                    // 평균 값 계산
                    double averageValue = monthlyRecords.stream()
                            .mapToDouble(customRecord -> parseValue(customRecord.getValue()))
                            .average()
                            .orElse(0.0);


                    return MonthlyRecordResponseDto.builder()
                            .recordTypeId(customRecordTypeId)  // Custom Record Type ID 추가
                            .month(month.toString() + "-01")
                            .participationCount((int) participationCount)
                            .averageValue(averageValue)
                            .build();
                })
                .collect(Collectors.toList());

        int start = Math.min((int) pageable.getOffset(), summary.size());
        int end = Math.min((start + pageable.getPageSize()), summary.size());

        List<MonthlyRecordResponseDto> pagedSummary = summary.subList(start, end);

        return new PageImpl<>(pagedSummary, pageable, summary.size());
    }

    private double parseValue(String jsonValue) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonValue);

            if (rootNode.has("amount")) {
                return extractNumericValue(rootNode.get("amount").asText());
            } else if (rootNode.has("hours")) {
                return extractNumericValue(rootNode.get("hours").asText());
            } else if (rootNode.has("weight")) {
                return extractNumericValue(rootNode.get("weight").asText());
            } else if (rootNode.has("reps")) {
                return extractNumericValue(rootNode.get("reps").asText());
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    private double extractNumericValue(String value) {
        try {
            String numericValue = value.replaceAll("[^0-9.]", "");
            return Double.parseDouble(numericValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
