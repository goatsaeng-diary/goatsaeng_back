package com.example.gotsaeng_back.domain.record.controller;


import com.example.gotsaeng_back.domain.record.dto.request.CustomRecordTypeRequestDto;
import com.example.gotsaeng_back.domain.record.dto.response.CustomRecordTypeDto;
import com.example.gotsaeng_back.domain.record.entity.CustomRecordType;
import com.example.gotsaeng_back.domain.record.entity.RecordType;
import com.example.gotsaeng_back.domain.record.service.CustomRecordTypeService;
import com.example.gotsaeng_back.global.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
public class CustomRecordTypeController {
    private final CustomRecordTypeService customRecordTypeService;

    //생성
    @PostMapping("/custom-type")
    public CustomResponse<CustomRecordType> showRecordType(@RequestHeader("Authorization") String token,
                                                           @RequestBody CustomRecordTypeRequestDto dto) {
        CustomRecordType customRecordType = customRecordTypeService.
        return new CustomResponse<>(HttpStatus.OK, "기본 기록 단일 조회",);
    }

    //수정
    @PutMapping("/custom-type/{customRecordTypeId}")
    public CustomResponse<CustomRecordType> showRecordType(@PathVariable("customRecordTypeId") Long customRecordTypeId,
                                                           @RequestHeader("Authorization") String token,
                                                           @RequestBody CustomRecordTypeRequestDto dto) {
        return new CustomResponse<>(HttpStatus.OK, "기본 기록 단일 조회",);
    }

    //삭제
    @DeleteMapping("/custom-type/{customRecordTypeId}")
    public CustomResponse<Void> showRecordType(@PathVariable("customRecordTypeId") Long customRecordTypeId,
                                                           @RequestHeader("Authorization") String token) {
        return new CustomResponse<>(HttpStatus.OK, "기본 기록 단일 조회",null);
    }

    //단일 조회
    @GetMapping("/custom-type/{customRecordTypeId}")
    public CustomResponse<CustomRecordTypeDto> showRecordType(@PathVariable("customRecordTypeId") Long customRecordTypeId,
                                                           @RequestHeader("Authorization") String token) {
        return new CustomResponse<>(HttpStatus.OK, "기본 기록 단일 조회",);
    }

    //전체 조회 - user 본인이 만든 거
    @GetMapping("/custom-type")
    public CustomResponse<List<CustomRecordTypeDto>> showAllRecordType(@RequestHeader("Authorization") String token) {
        return new CustomResponse<>(HttpStatus.OK, "기본 기록 단일 조회",);
    }
}
