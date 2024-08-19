package com.example.gotsaeng_back.global.gptapi;

import com.example.gotsaeng_back.global.gptapi.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WordRepository extends JpaRepository<Word,Long> {
    @Query("SELECT COUNT(w) FROM Word w")
    long countWords();
}
