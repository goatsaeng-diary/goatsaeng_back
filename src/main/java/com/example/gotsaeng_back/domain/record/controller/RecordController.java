package com.example.gotsaeng_back.domain.record.controller;

import com.example.gotsaeng_back.domain.record.dto.request.RecordRequestDto;
import com.example.gotsaeng_back.domain.record.dto.response.MonthlyRecordResponseDto;
import com.example.gotsaeng_back.domain.record.dto.response.RecordResponseDto;
import com.example.gotsaeng_back.domain.record.entity.Record;
import com.example.gotsaeng_back.domain.record.service.RecordService;
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
@RequestMapping("/api/record")
public class RecordController {
    private final RecordService recordService;

    //생성
    @PostMapping("/default/{recordTypeId}")
    public CustomResponse<RecordResponseDto> createRecord(@PathVariable("recordTypeId") Long recordTypeId,
                                                          @RequestBody RecordRequestDto dto,
                                                          @RequestHeader("Authorization") String token) {
        Record record = recordService.saveRecord(dto, token, recordTypeId);
        RecordResponseDto responseDto = RecordResponseDto.fromEntity(record);
        return new CustomResponse<>(HttpStatus.OK, "기록을 저장했습니다.", responseDto);
    }

    //수정
    @PutMapping("/default/{recordId}")
    public CustomResponse<RecordResponseDto> updateRecord(@PathVariable("recordId") Long recordId,
                                                          @RequestBody RecordRequestDto dto,
                                                          @RequestHeader("Authorization") String token) {
        Record record = recordService.updateRecord(dto, token, recordId);
        RecordResponseDto responseDto = RecordResponseDto.fromEntity(record);
        return new CustomResponse<>(HttpStatus.OK, "기록을 수정했습니다.", responseDto);
    }

    //삭제
    @DeleteMapping("/default/{recordId}")
    public CustomResponse<Void> deleteRecord(@PathVariable("recordId") Long recordId,
                                             @RequestHeader("Authorization") String token) {
        recordService.deleteRecord(token, recordId);
        return new CustomResponse<>(HttpStatus.OK, "기록을 삭제했습니다.", null);
    }

    //단일 조회
    @GetMapping("/default/{recordId}")
    public CustomResponse<RecordResponseDto> getRecordById(@PathVariable("recordId") Long recordId) {

        Record record = recordService.findByRecordId(recordId);
        RecordResponseDto responseDto = RecordResponseDto.fromEntity(record);
        return new CustomResponse<>(HttpStatus.OK, "기록을 조회했습니다.", responseDto);
    }

    //단일 조회 -- user정보를 통해 오늘 기록
    @GetMapping("/default/today")
    public CustomResponse<List<RecordResponseDto>> getRecordByDate(@RequestHeader("Authorization") String token) {
        List<Record> recordList  = recordService.findByRecordUser(token);
        List<RecordResponseDto> responseDto = recordList.stream()
                .map(RecordResponseDto::fromEntity)
                .collect(Collectors.toList());
        return new CustomResponse<>(HttpStatus.OK, "기록을 조회했습니다.", responseDto);
    }

    //당월 조회
    @GetMapping("/default/current-month")
    public CustomResponse<Page<RecordResponseDto>> getRecordsForCurrentMonth(
            @RequestHeader("Authorization") String token,
            Pageable pageable) {
        Page<RecordResponseDto> recordList = recordService.getRecordForCurrentMonth(token, pageable);
        return new CustomResponse<>(HttpStatus.OK, "당월 기록을 조회했습니다.", recordList);
    }

    //전체 기록을 월별로 조회 (평균치 계산)
    @GetMapping("/default/prev-average-month/{recordTypeId}")
    public CustomResponse<Page<MonthlyRecordResponseDto>> getRecordsByMonth(
            @PathVariable("recordTypeId") Long recordTypeId,
            @RequestHeader("Authorization") String token,
            Pageable pageable) {
        Page<MonthlyRecordResponseDto> monthlyRecordList = recordService.getRecordsByMonth(token, recordTypeId, pageable);
        return new CustomResponse<>(HttpStatus.OK, "월별 기록을 조회했습니다.", monthlyRecordList);
    }
}