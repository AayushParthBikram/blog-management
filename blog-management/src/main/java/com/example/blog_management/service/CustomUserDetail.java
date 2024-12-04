package com.example.blog_management.service;

import com.example.blog_management.model.User;
import com.example.blog_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
public class CustomUserDetail implements UserDetailsService {

    private static final Logger logger = Logger.getLogger(CustomUserDetail.class.getName());

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetail(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user by username: " + username);

        // Fetch user from repository
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Check if roles are assigned properly
        if (user.getRole_access() == null || user.getRole_access().isEmpty()) {
            logger.warning("User roles are empty for user: " + username);
            throw new UsernameNotFoundException("User roles are empty for user: " + username);
        }

        // Convert role set to granted authorities
        var authorities = user.getRole_access().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        logger.info("User roles assigned to " + username + ": " + authorities);

        // Return UserDetails with username, password, and authorities
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
