package com.example.gotsaeng_back.domain.auth.service;

import com.example.gotsaeng_back.domain.auth.entity.EmailValid;

public interface EmailService {
    EmailValid findByEmail(String email);
    EmailValid saveEmailAndCode(String email , String code);
    void deleteByEmail(String email);
}
