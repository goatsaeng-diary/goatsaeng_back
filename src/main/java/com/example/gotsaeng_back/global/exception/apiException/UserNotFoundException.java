package com.example.gotsaeng_back.global.exception.apiException;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
