package com.example.gotsaeng_back.auth.service;

import com.example.gotsaeng_back.auth.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {

    User findByUsername(String username);
    User regUser(User user);
    List<User> userList();
}
