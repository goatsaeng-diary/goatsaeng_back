package com.example.gotsaeng_back.domain.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/")
    public String test(){
        return "login";
    }
    @GetMapping("/success")
    public String success(){
        return "success";
    }
}
