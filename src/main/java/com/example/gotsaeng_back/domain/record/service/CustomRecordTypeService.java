package com.example.gotsaeng_back.domain.record.service;


import com.example.gotsaeng_back.domain.record.dto.request.CustomRecordTypeRequestDto;
import com.example.gotsaeng_back.domain.record.entity.CustomRecordType;

import java.util.List;

public interface CustomRecordTypeService {
    CustomRecordType saveCustomRecordType(String token, CustomRecordTypeRequestDto dto);
    CustomRecordType updateCustomRecordType(Long customRecordTypeId, String token, CustomRecordTypeRequestDto dto);
    void deleteCustomRecordType(Long customRecordTypeId, String token);
    CustomRecordType findByCustomRecordTypeId(Long customRecordTypeId);
    List<CustomRecordType> showAllCustomRecordType(String token);
}
