package com.example.gotsaeng_back.global.response;

import com.example.gotsaeng_back.global.exception.ApiExceptionEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    private HttpStatus status;
    private ApiExceptionEntity exception;

    // 실패
    public ErrorResponse(HttpStatus status, ApiExceptionEntity exception) {
        this.status = status;
        this.exception = exception;
    }
}