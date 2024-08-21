package com.example.gotsaeng_back.global.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
public class ApiExceptionEntity {
    private String errorCode;
    private String errorMessage;

    public ApiExceptionEntity(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}