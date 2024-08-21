package com.example.gotsaeng_back.domain.record.dto.response;

import com.example.gotsaeng_back.domain.record.entity.CustomRecordType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomRecordTypeResponseDto {
    private Long customRecordTypeId;
    private String typeName;
    private String unit;
    private String field;

    public static CustomRecordTypeResponseDto fromEntity(CustomRecordType customRecordType) {
        return CustomRecordTypeResponseDto.builder()
                .customRecordTypeId(customRecordType.getCustomRecordTypeId())
                .typeName(customRecordType.getTypeName())
                .unit(customRecordType.getUnit())
                .field(customRecordType.getField())
                .build();
    }
}
