package com.example.gotsaeng_back.domain.record.controller;


import com.example.gotsaeng_back.domain.record.dto.request.CustomRecordTypeRequestDto;
import com.example.gotsaeng_back.domain.record.dto.response.CustomRecordTypeResponseDto;
import com.example.gotsaeng_back.domain.record.entity.CustomRecordType;
import com.example.gotsaeng_back.domain.record.service.CustomRecordTypeService;
import com.example.gotsaeng_back.global.response.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
public class CustomRecordTypeController {
    private final CustomRecordTypeService customRecordTypeService;

    //생성
    @PostMapping("/custom-type")
    public CustomResponse<CustomRecordTypeResponseDto> createRecordType(@RequestHeader("Authorization") String token,
                                                                      @RequestBody CustomRecordTypeRequestDto dto) {
        CustomRecordType customRecordType = customRecordTypeService.saveCustomRecordType(token, dto);
        CustomRecordTypeResponseDto responseDto = CustomRecordTypeResponseDto.fromEntity(customRecordType);
        return new CustomResponse<>(HttpStatus.OK, "Custom 기록이 생성되었습니다.", responseDto);
    }

    //수정
    @PutMapping("/custom-type/{customRecordTypeId}")
    public CustomResponse<CustomRecordTypeResponseDto> updateRecordType(@PathVariable("customRecordTypeId") Long customRecordTypeId,
                                                           @RequestHeader("Authorization") String token,
                                                           @RequestBody CustomRecordTypeRequestDto dto) {
        CustomRecordType customRecordType = customRecordTypeService.updateCustomRecordType(customRecordTypeId, token, dto);
        CustomRecordTypeResponseDto responseDto = CustomRecordTypeResponseDto.fromEntity(customRecordType);
        return new CustomResponse<>(HttpStatus.OK, "Custom 기록이 수정되었습니다.", responseDto);
    }

    //삭제
    @DeleteMapping("/custom-type/{customRecordTypeId}")
    public CustomResponse<Void> deleteRecordType(@PathVariable("customRecordTypeId") Long customRecordTypeId,
                                                           @RequestHeader("Authorization") String token) {
        customRecordTypeService.deleteCustomRecordType(customRecordTypeId, token);
        return new CustomResponse<>(HttpStatus.OK, "Custom 기록이 삭제되었습니다.",null);
    }

    //단일 조회
    @GetMapping("/custom-type/{customRecordTypeId}")
    public CustomResponse<CustomRecordTypeResponseDto> showRecordType(@PathVariable("customRecordTypeId") Long customRecordTypeId) {
        CustomRecordType customRecordType = customRecordTypeService.findByCustomRecordTypeId(customRecordTypeId);
        CustomRecordTypeResponseDto responseDto = CustomRecordTypeResponseDto.fromEntity(customRecordType);
        return new CustomResponse<>(HttpStatus.OK, "Custom 기록 단일 조회", responseDto);
    }

    //전체 조회 - user 본인이 만든 거
    @GetMapping("/custom-type")
    public CustomResponse<List<CustomRecordTypeResponseDto>> showAllRecordType(@RequestHeader("Authorization") String token) {
        List<CustomRecordType> customRecordTypeList = customRecordTypeService.showAllCustomRecordType(token);
        List<CustomRecordTypeResponseDto> responseDto = customRecordTypeList.stream()
                .map(CustomRecordTypeResponseDto::fromEntity)
                .collect(Collectors.toList());
        return new CustomResponse<>(HttpStatus.OK, "Custom 기록 전체 조회", responseDto);
    }
}
