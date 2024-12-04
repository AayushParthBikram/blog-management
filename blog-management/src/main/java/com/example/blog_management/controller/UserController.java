package com.example.blog_management.controller;

import com.example.blog_management.exception.InvalidCredentialsException;
import com.example.blog_management.exception.ResourceNotFoundException;
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

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Register a new user.
     *
     * @param user User details sent in the request body and then Server takes this data and deserialized it as JSON Object
     * @return ResponseEntity with registered User and HTTP status 201 CREATED
     */
    @PostMapping("/register")
    @PermitAll  // Allow open registration without any role restriction
    public ResponseEntity<User> register(@Validated @RequestBody User user) {
        try {
            User registeredUser = userService.register(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error during user registration: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * To log in to this application, the user needs two credentials: Username and Password.
     * @param username Username of the user
     * @param password Password of the user
     * @return ResponseEntity with logged-in user details
     */
    @PostMapping("/login")
    @PermitAll // Anyone Can Log in No restrictions for the role
    public ResponseEntity<User> login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        logger.info("User attempting to log in: {}", username);
        try {
            User loggedUser = userService.login(username, password);
            logger.info("Login successful for user: {}", username);
            return new ResponseEntity<>(loggedUser, HttpStatus.OK);
        } catch (InvalidCredentialsException e) {
            logger.error("Login failed for user: {}", username, e);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error("Login failed for user: {}", username, e);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/resetPassword")
    @PreAuthorize("hasRole('ADMIN')")  // Only ADMIN can reset passwords
    public ResponseEntity<String> resetPassword(@RequestParam String username) {
        try {
            userService.resetPassword(username);
            logger.info("Password reset successfully for user: {}", username);
            return new ResponseEntity<>("Password reset successfully.", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            logger.error("User not found for password reset: {}", username);
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error resetting password for user: {}", username);
            return new ResponseEntity<>("Failed to reset password", HttpStatus.BAD_REQUEST);
        }
    }
}
