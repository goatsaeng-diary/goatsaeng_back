package com.example.gotsaeng_back.domain.record.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomRecordRequestDto {
    @NotNull
    private String value;
}
