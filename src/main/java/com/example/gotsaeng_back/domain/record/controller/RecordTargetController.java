package com.example.gotsaeng_back.domain.record.controller;

import com.example.gotsaeng_back.domain.record.dto.request.RecordRequestDto;
import com.example.gotsaeng_back.domain.record.dto.request.RecordTargetRequestDto;
import com.example.gotsaeng_back.domain.record.dto.response.RecordResponseDto;
import com.example.gotsaeng_back.domain.record.dto.response.RecordTargetDto;
import com.example.gotsaeng_back.domain.record.entity.Record;
import com.example.gotsaeng_back.domain.record.entity.RecordTarget;
import com.example.gotsaeng_back.domain.record.service.RecordTargetService;
import com.example.gotsaeng_back.global.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
public class RecordTargetController {
    private final RecordTargetService recordTargetService;

    //생성
    @PostMapping("/target")
    public CustomResponse<RecordTargetDto> createTarget(@RequestBody RecordTargetRequestDto dto,
                                                        @RequestHeader("Authorization") String token) {

        return new CustomResponse<>(HttpStatus.OK, "기록 목표를 저장했습니다.", );
    }

    //수정
    @PutMapping("/target/{targetId}")
    public CustomResponse<RecordTargetDto> updateTarget(@PathVariable("targetId") Long targetId,
                                                        @RequestBody RecordTargetRequestDto dto,
                                                        @RequestHeader("Authorization") String token) {

        return new CustomResponse<>(HttpStatus.OK, "기록 목표를 수정했습니다.", );
    }

    //단일 조회 - 기본
    @GetMapping("/target/default/{recordTypeId}")
    public CustomResponse<RecordTargetDto> showDefaultTarget(@PathVariable("recordTypeId") Long recordTypeId,
                                                        @RequestBody RecordTargetRequestDto dto,
                                                        @RequestHeader("Authorization") String token) {

        return new CustomResponse<>(HttpStatus.OK, "기본 기록 목표를 조회했습니다.", );
    }

    //단일 조회 - 커스텀
    @GetMapping("/target/custom/{customRecordTypeId}")
    public CustomResponse<RecordTargetDto> showCustomTarget(@PathVariable("customRecordTypeId") Long customRecordTypeId,
                                                        @RequestBody RecordTargetRequestDto dto,
                                                        @RequestHeader("Authorization") String token) {

        return new CustomResponse<>(HttpStatus.OK, "커스텀 기록 목표를 조회했습니다.", );
    }

    //전체 조회 - 기본
    @GetMapping("/target/allDefault/{recordTypeId}")
    public CustomResponse<RecordTargetDto> showDefaultAllTarget(@PathVariable("recordTypeId") Long recordTypeId,
                                                        @RequestBody RecordTargetRequestDto dto,
                                                        @RequestHeader("Authorization") String token) {

        return new CustomResponse<>(HttpStatus.OK, "기본 기록 목표를 조회했습니다.", );
    }

    //전체 조회 - 커스텀
    @GetMapping("/target/allCustom/{customRecordTypeId}")
    public CustomResponse<RecordTargetDto> showCustomAllTarget(@PathVariable("customRecordTypeId") Long customRecordTypeId,
                                                        @RequestBody RecordTargetRequestDto dto,
                                                        @RequestHeader("Authorization") String token) {

        return new CustomResponse<>(HttpStatus.OK, "기본 기록 목표를 조회했습니다.", );
    }
}
