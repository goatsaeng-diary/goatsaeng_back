package com.example.gotsaeng_back.domain.record.controller;

import com.example.gotsaeng_back.domain.record.entity.RecordType;
import com.example.gotsaeng_back.domain.record.service.RecordTypeService;
import com.example.gotsaeng_back.global.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
public class RecordTypeController {
    private final RecordTypeService recordTypeService;

    //단일 조회
    @GetMapping("/default-type/{recordTypeId}")
    public CustomResponse<RecordType> showRecordType(@PathVariable("recordTypeId") Long recordTypeId) {
        RecordType type = recordTypeService.getRecordTypeByRecordTypeId(recordTypeId);
        return new CustomResponse<>(HttpStatus.OK, "기본 기록 단일 조회", type);
    }

    //전체 조회
    @GetMapping("/default-type")
    public CustomResponse<List<RecordType>> showAllRecordType() {
        List<RecordType> typeList = recordTypeService.getAllRecordTypes();
        return new CustomResponse<>(HttpStatus.OK, "기본 기록 단일 조회", typeList);
    }
}
