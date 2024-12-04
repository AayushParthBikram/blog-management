package com.example.blog_management.config;

import com.example.blog_management.service.CustomUserDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetail customUserDetail;


    public SecurityConfig(CustomUserDetail customUserDetail) {
        this.customUserDetail = customUserDetail;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF for this demo project
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register").permitAll() // Temporarily allow public access for registration
                        .requestMatchers("/api/users/login").permitAll() // Public access for login
                        .requestMatchers("/api/blogposts/**").hasAnyRole("USER", "ADMIN") // Restrict to USER or ADMIN
                        .requestMatchers("/api/comments/**").authenticated() // Require authentication for comments
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .httpBasic(httpBasic -> httpBasic.realmName("Blog Management API")); // Enable HTTP Basic authentication

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetail;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}