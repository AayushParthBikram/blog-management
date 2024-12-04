package com.example.blog_management.controller;

import com.example.blog_management.model.User;
import com.example.blog_management.service.UserService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController // Indicates this class handles REST API endpoints
@RequestMapping("/api/users") // Base path for all user-related endpoints
public class UserController {


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService; // Injects the UserService interface

    /**
     * Register a new user.
     *
     * @param user User details sent in the request body and then Server takes this data and deserialized it as JSON Object
     * @return ResponseEntity with registered User and HTTP status 201 CREATED
     */
    @PostMapping("/register")
    @PermitAll //No restrictions for registering to the application.
        public ResponseEntity<User> register(@Validated
            @RequestBody User user) {
        User registeredUser = userService.register(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    /**
     * To make a Login to this Application we need two credentials One is the Username and another is the password.
     * So we passed here two things as parameters.
     * @param username Username of the user
     * @param password Password of the user
     *
     */
    @PostMapping("/login")
    @PermitAll // Anyone Can Log in No restrictions for the role
    public ResponseEntity<User> login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        logger.info("User attempting to log in: {}", username);
        User loggedUser = userService.login(username, password);
        return new ResponseEntity<>(loggedUser, HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> resetPassword(@RequestParam String username) {
        try {
            userService.resetPassword(username);
            logger.info("Password reset successfully for user: {}", username);
            return new ResponseEntity<>("Password reset successfully.", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error resetting password for user: {}", username);
            return new ResponseEntity<>("Failed to reset password", HttpStatus.BAD_REQUEST);
        }
    }



}