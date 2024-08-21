package com.example.gotsaeng_back.domain.auth.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpDto {

    private String username;
    private String name;
    private LocalDate birthDate;
    private String password;
    private String email;
    private String nickname;
}
