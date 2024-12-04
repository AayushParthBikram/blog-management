package com.example.blog_management.service;

import com.example.blog_management.exception.InvalidCredentialsException;
import com.example.blog_management.exception.ResourceNotFoundException;
import com.example.blog_management.model.User;
import com.example.blog_management.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
        logger.debug("Entering register method with user: {}", user);

        // Ensure role_access is initialized (if not provided)
        Set<String> roles = user.getRole_access() != null ? user.getRole_access() : new HashSet<>();

        // Check if there are no users in the database, and add ROLE_ADMIN to the first user
        if (userRepository.count() == 0) {
            if (!roles.contains("ROLE_ADMIN")) {
                roles.add("ROLE_ADMIN");  // First user gets admin privileges
            }
        } else {
            if (!roles.contains("ROLE_USER")) {
                roles.add("ROLE_USER");  // Subsequent users get USER role
            }
        }

        user.setRole_access(roles);

        // Encode the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user and return the result
        logger.debug("Saving user: {}", user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) {
        logger.debug("Entering login method with username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate the password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        logger.debug("User logged in successfully: {}", username);
        return user;
    }

    @Override
    public void resetPassword(String username) {
        logger.debug("Entering resetPassword method for username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        String temporaryPassword = generateTemporaryPassword();
        String encryptedPassword = passwordEncoder.encode(temporaryPassword);
        user.setPassword(encryptedPassword);

        // Save the user with the new temporary password
        userRepository.save(user);

        logger.info("Temporary password for user {}: {}", username, temporaryPassword);
        logger.info("Password reset successfully for user: {}", username);
    }

    private String generateTemporaryPassword() {
        logger.debug("Generating temporary password");

        int length = 8;
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$!";
        Random random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(characterSet.charAt(random.nextInt(characterSet.length())));
        }

        logger.debug("Temporary password generated: {}", password.toString());
        return password.toString();
    }
}
