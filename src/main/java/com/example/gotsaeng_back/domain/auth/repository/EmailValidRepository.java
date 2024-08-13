package com.example.gotsaeng_back.domain.auth.repository;

import com.example.gotsaeng_back.domain.auth.entity.EmailValid;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailValidRepository extends JpaRepository<EmailValid,Long> {
    Optional<?> findByEmail(String email);
    void deleteByEmail(String email);
}
