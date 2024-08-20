package com.example.gotsaeng_back.domain.record.service;


import com.example.gotsaeng_back.domain.record.dto.request.CustomRecordTypeRequestDto;
import com.example.gotsaeng_back.domain.record.dto.response.CustomRecordTypeDto;
import com.example.gotsaeng_back.domain.record.entity.CustomRecordType;

import java.util.List;

public interface CustomRecordTypeService {
    CustomRecordType saveCustomRecordType(String token, CustomRecordTypeRequestDto dto);
    CustomRecordType updateCustomRecordType(Long customRecordTypeId, String token, CustomRecordTypeRequestDto dto);
    void deleteCustomRecordType(Long customRecordTypeId, String token);
    CustomRecordType findByCustomRecordTypeId(Long customRecordTypeId);
    CustomRecordType showCustomRecordType(Long customRecordTypeId, String token);
    List<CustomRecordTypeDto> showAllCustomRecordType(String token);
}
