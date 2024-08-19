package com.example.gotsaeng_back.domain.record.service.impl;

import com.example.gotsaeng_back.domain.auth.entity.User;
import com.example.gotsaeng_back.domain.auth.service.UserService;
import com.example.gotsaeng_back.domain.record.dto.request.RecordRequestDto;
import com.example.gotsaeng_back.domain.record.dto.response.MonthlyRecordResponseDto;
import com.example.gotsaeng_back.domain.record.dto.response.RecordResponseDto;
import com.example.gotsaeng_back.domain.record.entity.Record;
import com.example.gotsaeng_back.domain.record.entity.RecordType;
import com.example.gotsaeng_back.domain.record.repository.RecordRepository;
import com.example.gotsaeng_back.domain.record.service.RecordService;
import com.example.gotsaeng_back.domain.record.service.RecordTypeService;
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
public class RecordServiceImpl implements RecordService {
    private final RecordRepository recordRepository;
    private final RecordTypeService recordTypeService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    //생성
    @Override
    @Transactional
    public Record saveRecord(RecordRequestDto dto, String token, Long recordTypeId) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);
        RecordType recordType = recordTypeService.getRecordTypeByRecordTypeId(recordTypeId);

        if (user == null || recordType == null) {
            throw new ApiException(ExceptionEnum.BAD_REQUEST);
        }

        //날짜 같으면 중복 등록이 아닌 수정으로 처리
        Record existingRecord = recordRepository.findByUserAndRecordTypeAndDate(user, recordType, LocalDate.now());

        if (existingRecord != null) {
            existingRecord.setValue(dto.getValue());
            return recordRepository.save(existingRecord);
        } else {
            // 기존 기록이 없으면 새로운 기록 생성
            Record record = Record.builder()
                    .user(user)
                    .recordType(recordType)
                    .date(dto.getDate())
                    .value(dto.getValue())
                    .build();

            return recordRepository.save(record);
        }
    }

    //수정
    @Override
    @Transactional
    public Record updateRecord(RecordRequestDto dto, String token, Long recordId) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        Record record = findByRecordId(recordId);

        if (!user.getUserId().equals(record.getUser().getUserId())) {
            throw new ApiException(ExceptionEnum.RECORD_UPDATE_FORBIDDEN);
        }

        record.setValue(dto.getValue());
        return recordRepository.save(record);
    }

    //삭제
    @Override
    @Transactional
    public void deleteRecord(String token, Long recordId) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        Record record = findByRecordId(recordId);

        if (!user.getUserId().equals(record.getUser().getUserId())) {
            throw new ApiException(ExceptionEnum.RECORD_DELETE_FORBIDDEN);
        }

        recordRepository.deleteById(recordId);
    }

    //단일 조회
    @Override
    @Transactional(readOnly = true)
    public Record findByRecordId(Long recordId) {
        return recordRepository.findById(recordId).orElseThrow(() -> new ApiException(ExceptionEnum.RECORD_NOT_FOUND));
    }

    //단일 조회 - username
    @Override
    @Transactional(readOnly = true)
    public Record findByRecordUser(String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }
        return recordRepository.findByUser(user, LocalDate.now());}

    //전체 조회
    @Transactional(readOnly = true)
    public List<Record> getAllRecordByUser(User user) {
        return recordRepository.findAllByUser(user);
    }

    //당월 조회
    @Override
    @Transactional(readOnly = true)
    public Page<RecordResponseDto> getRecordForCurrentMonth(String token, Pageable pageable) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        LocalDate start = LocalDate.now().withDayOfMonth(1); // 시작 일
        LocalDate end = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()); // 마지막 일

        Page<Record> recordPage = recordRepository.findAllByUserAndDateBetween(user, start, end, pageable);

        return recordPage.map(record -> RecordResponseDto.builder()
                .recordId(record.getRecordId())
                .recordTypeId(record.getRecordType().getRecordTypeId())
                .date(record.getDate())
                .value(record.getValue())
                .build());
    }


    //전체 기록을 월별로 조회 (평균치 계산)
    @Override
    @Transactional(readOnly = true)
    public Page<MonthlyRecordResponseDto> getRecordsByMonth(String token, Pageable pageable) {

        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.findById(userId);

        if (user == null) {
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
        }

        List<Record> recordList = getAllRecordByUser(user);

        //기록이 있는 월 수집
        Map<LocalDate, List<Record>> recordsByMonth = recordList.stream()
                .collect(Collectors.groupingBy(record -> LocalDate.of(record.getDate().getYear(), record.getDate().getMonth(), 1)));

        List<MonthlyRecordResponseDto> summary = recordsByMonth.entrySet().stream()
                .map(entry -> {
                    LocalDate month = entry.getKey();
                    List<Record> monthlyRecords = entry.getValue();

                    // 참여 횟수
                    long participationCount = monthlyRecords.size();

                    // 평균 값 계산
                    double averageValue = monthlyRecords.stream()
                            .mapToDouble(record -> parseValue(record.getValue()))
                            .average()
                            .orElse(0.0);

                    return MonthlyRecordResponseDto.builder()
                            .month(month)
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