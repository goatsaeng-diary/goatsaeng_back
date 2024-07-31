package com.example.gotsaeng_back.global.exception.apiException;

import lombok.Getter;

@Getter
public class CustomJwtException extends RuntimeException {
    private final JwtExceptionCode jwtExceptionCode;

    public CustomJwtException(JwtExceptionCode jwtExceptionCode) {
        super(jwtExceptionCode.getMessage());
        this.jwtExceptionCode = jwtExceptionCode;
    }
}
