package com.example.gotsaeng_back.domain.record.dto.response;

import com.example.gotsaeng_back.domain.record.entity.CustomRecordType;
import com.example.gotsaeng_back.domain.record.entity.Record;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomRecordTypeDto {
    private Long customRecordTypeId;
    private String typeName;
    private String unit;
    private String field;

    public static CustomRecordTypeDto fromEntity(CustomRecordType customRecordType) {
        return CustomRecordTypeDto.builder()
                .customRecordTypeId(customRecordType.getCustomRecordTypeId())
                .typeName(customRecordType.getTypeName())
                .unit(customRecordType.getUnit())
                .field(customRecordType.getField())
                .build();
    }
}
