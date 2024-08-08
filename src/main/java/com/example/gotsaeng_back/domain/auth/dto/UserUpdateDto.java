package com.example.gotsaeng_back.domain.auth.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
    private Long userId;
    private String username;
    private String name;
    private String email;
    private LocalDate birthDate;

}
