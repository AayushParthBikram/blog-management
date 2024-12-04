package com.example.blog_management.service;

import com.example.blog_management.model.User;

public interface UserService {
    User register(User user);
    User login(String username, String password);


    void resetPassword(String username);
}
