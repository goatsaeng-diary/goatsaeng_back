package com.example.gotsaeng_back.domain.record.dto.request;

import com.example.gotsaeng_back.domain.record.entity.CustomRecordType;
import com.example.gotsaeng_back.domain.record.entity.RecordType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomRecordTypeRequestDto {
    @NotNull
    private String typeName;
    @NotNull
    private String unit;
    @NotNull
    private String field;
}
