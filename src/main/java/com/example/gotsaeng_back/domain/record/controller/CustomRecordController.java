package com.example.gotsaeng_back.domain.record.controller;

import com.example.gotsaeng_back.domain.record.dto.request.CustomRecordRequestDto;
import com.example.gotsaeng_back.domain.record.dto.response.CustomRecordResponseDto;
import com.example.gotsaeng_back.domain.record.dto.response.MonthlyRecordResponseDto;
import com.example.gotsaeng_back.domain.record.entity.CustomRecord;
import com.example.gotsaeng_back.domain.record.service.CustomRecordService;
import com.example.gotsaeng_back.global.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customRecord")
public class CustomRecordController {
    private final CustomRecordService customRecordService;

    // 생성
    @PostMapping("/custom/{customRecordTypeId}")
    public CustomResponse<CustomRecordResponseDto> createCustomRecord(@PathVariable("customRecordTypeId") Long customRecordTypeId,
                                                                      @RequestBody CustomRecordRequestDto dto,
                                                                      @RequestHeader("Authorization") String token) {
        CustomRecord customRecord = customRecordService.saveCustomRecord(dto, token, customRecordTypeId);
        CustomRecordResponseDto responseDto = CustomRecordResponseDto.fromEntity(customRecord);
        return new CustomResponse<>(HttpStatus.OK, "기록을 저장했습니다.", responseDto);
    }

    // 수정
    @PutMapping("/custom/{customRecordId}")
    public CustomResponse<CustomRecordResponseDto> updateCustomRecord(@PathVariable("customRecordId") Long customRecordId,
                                                                      @RequestBody CustomRecordRequestDto dto,
                                                                      @RequestHeader("Authorization") String token) {
        CustomRecord customRecord = customRecordService.updateCustomRecord(dto, token, customRecordId);
        CustomRecordResponseDto responseDto = CustomRecordResponseDto.fromEntity(customRecord);
        return new CustomResponse<>(HttpStatus.OK, "기록을 수정했습니다.", responseDto);
    }

    // 삭제
    @DeleteMapping("/custom/{customRecordId}")
    public CustomResponse<Void> deleteCustomRecord(@PathVariable("customRecordId") Long customRecordId,
                                                   @RequestHeader("Authorization") String token) {
        customRecordService.deleteCustomRecord(token, customRecordId);
        return new CustomResponse<>(HttpStatus.OK, "기록을 삭제했습니다.", null);
    }

    // 단일 조회
    @GetMapping("/custom/{customRecordId}")
    public CustomResponse<CustomRecordResponseDto> getCustomRecordById(@PathVariable("customRecordId") Long customRecordId) {
        CustomRecord customRecord = customRecordService.findByCustomRecordId(customRecordId);
        CustomRecordResponseDto responseDto = CustomRecordResponseDto.fromEntity(customRecord);
        return new CustomResponse<>(HttpStatus.OK, "기록을 조회했습니다.", responseDto);
    }

    // 단일 조회 -- user정보를 통해 오늘 기록
    @GetMapping("/custom/today")
    public CustomResponse<List<CustomRecordResponseDto>> getCustomRecordByDate(@RequestHeader("Authorization") String token) {
        List<CustomRecord> customRecordList = customRecordService.findByCustomRecordUser(token);
        List<CustomRecordResponseDto> responseDto = customRecordList.stream()
                .map(CustomRecordResponseDto::fromEntity)
                .collect(Collectors.toList());
        return new CustomResponse<>(HttpStatus.OK, "기록을 조회했습니다.", responseDto);
    }

    // 당월 조회
    @GetMapping("/custom/current-month")
    public CustomResponse<Page<CustomRecordResponseDto>> getCustomRecordsForCurrentMonth(
            @RequestHeader("Authorization") String token,
            Pageable pageable) {
        Page<CustomRecordResponseDto> customRecordList = customRecordService.getCustomRecordForCurrentMonth(token, pageable);
        return new CustomResponse<>(HttpStatus.OK, "당월 기록을 조회했습니다.", customRecordList);
    }

    // 전체 기록을 월별로 조회 (평균치 계산)
    @GetMapping("/custom/prev-average-month/{customRecordTypeId}")
    public CustomResponse<Page<MonthlyRecordResponseDto>> getCustomRecordsByMonth(
            @PathVariable("customRecordTypeId") Long customRecordTypeId,
            @RequestHeader("Authorization") String token,
            Pageable pageable) {
        Page<MonthlyRecordResponseDto> monthlyCustomRecordList = customRecordService.getCustomRecordsByMonth(token, customRecordTypeId, pageable);
        return new CustomResponse<>(HttpStatus.OK, "월별 기록을 조회했습니다.", monthlyCustomRecordList);
    }
}
