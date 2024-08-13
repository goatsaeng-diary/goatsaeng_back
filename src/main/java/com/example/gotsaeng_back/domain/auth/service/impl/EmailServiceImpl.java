package com.example.gotsaeng_back.domain.auth.service.impl;

import com.example.gotsaeng_back.domain.auth.entity.EmailValid;
import com.example.gotsaeng_back.domain.auth.repository.EmailValidRepository;
import com.example.gotsaeng_back.domain.auth.service.EmailService;
import com.example.gotsaeng_back.global.exception.ApiException;
import com.example.gotsaeng_back.global.exception.ExceptionEnum;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final EmailValidRepository repository;
    @Override
    public EmailValid findByEmail(String email) {
        return (EmailValid) repository.findByEmail(email).orElseThrow(()->new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION));
    }

    @Transactional
    @Override
    public EmailValid saveEmailAndCode(String email, String code) {
        EmailValid emailValid = new EmailValid();
        emailValid.setEmail(email);
        emailValid.setCode(code);
        return repository.save(emailValid);
    }

    @Transactional
    @Override
    public void deleteByEmail(String email) {
        repository.deleteByEmail(email);
    }
}
