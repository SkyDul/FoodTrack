package com.foodtrack.web;

import com.foodtrack.entity.Petani;
import com.foodtrack.service.PetaniService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor


/**
 * Web Controller untuk LoginTestController.
 * Menangani request HTTP (GET/POST) dan mengatur respons antarmuka pengguna (View).
 */
public class LoginTestController {

    private final PetaniService petaniService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/api/test-login")
    public String testLogin() {
        try {
            var userDetails = userDetailsService.loadUserByUsername("test1");
            boolean match = passwordEncoder.matches("test", userDetails.getPassword());
            return "Loaded user: " + userDetails.getUsername() + ", match: " + match;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}

