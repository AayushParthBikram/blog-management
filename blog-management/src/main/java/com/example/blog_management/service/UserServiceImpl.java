package com.example.blog_management.service;

import com.example.blog_management.controller.UserController;
import com.example.blog_management.exception.ResourceNotFoundException;
import com.example.blog_management.model.User;
import com.example.blog_management.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        user.setRawPassword(passwordEncoder.encode(user.getRawPassword()));
        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found")); //for login into the application we need two params one is username and another is the password.
        if (!passwordEncoder.matches(password, user.getRawPassword())) {
            throw new RuntimeException("Invalid credentials"); //Here passwordEncoder is the Encrypt password used to authenticate the user.
        }
        return user;
    }

    @Override
    public void resetPassword(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        String temporaryPassword = generateTemporaryPassword();

        // Encrypt the temporary password (ensure password storage is secure)
        String encryptedPassword = passwordEncoder.encode(temporaryPassword);

        user.setRawPassword(encryptedPassword);
        userRepository.save(user);

        logger.info("Temporary password for user {}: {}", username, temporaryPassword);

        logger.info("Password reset successfully for user: {}", username);

    }

    private String generateTemporaryPassword() {
        // Generate a random alphanumeric password with at least 8 characters
        int length = 8;
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$!";
        Random random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(characterSet.charAt(random.nextInt(characterSet.length())));
        }

        return password.toString();
    }

}
