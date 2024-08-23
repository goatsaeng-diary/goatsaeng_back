package com.example.gotsaeng_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GotsaengBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(GotsaengBackApplication.class, args);
    }

}
