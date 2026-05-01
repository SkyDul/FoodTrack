package com.foodtrack.config;

import com.foodtrack.entity.Admin;
import com.foodtrack.entity.Petani;
import com.foodtrack.repository.AdminRepository;
import com.foodtrack.repository.PetaniRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AdminRepository adminRepository;
    private final PetaniRepository petaniRepository;
    private final com.foodtrack.service.LoginAttemptService loginAttemptService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            if (loginAttemptService.isBlocked(username)) {
                throw new org.springframework.security.authentication.LockedException("Akun terkunci karena 3x salah password. Tunggu " + loginAttemptService.getWaitTimeSeconds(username) + " detik.");
            }

            // 1. Try finding in Admin
            Optional<Admin> admin = adminRepository.findByUsername(username);
            if (admin.isPresent()) {
                return User.withUsername(admin.get().getUsername())
                    .password(admin.get().getPassword())
                    .authorities("ROLE_ADMIN")
                    .build();
            }

            // 2. Try finding in Petani
            Optional<Petani> petani = petaniRepository.findByUsername(username);
            if (petani.isPresent()) {
                return User.withUsername(petani.get().getUsername())
                    .password(petani.get().getPassword())
                    .authorities("ROLE_PETANI")
                    .build();
            }

            throw new UsernameNotFoundException("User tidak ditemukan: " + username);
        };
    }

    // ====== Admin Security Chain ======
    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/admin/**", "/admin")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/login").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
            )
            .formLogin(form -> form
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .defaultSuccessUrl("/admin/dashboard", true)
                .failureHandler((request, response, exception) -> {
                    String errorMessage = "Username atau kata sandi salah.";
                    if (exception instanceof org.springframework.security.authentication.LockedException) {
                        errorMessage = exception.getMessage();
                    }
                    response.sendRedirect("/admin/login?error=true&errorMsg=" + java.net.URLEncoder.encode(errorMessage, java.nio.charset.StandardCharsets.UTF_8));
                })
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .rememberMe(rm -> rm
                .key("sipangan_admin_secret_key_123")
                .rememberMeParameter("remember-me")
                .rememberMeCookieName("sipangan-admin-rm")
                .userDetailsService(userDetailsService())
                .tokenValiditySeconds(86400 * 30) // 30 days
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // ====== Petani Security Chain ======
    @Bean
    @Order(2)
    public SecurityFilterChain petaniFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/petani/**", "/login-petani")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login-petani").permitAll()
                .requestMatchers("/petani/**").hasRole("PETANI")
            )
            .formLogin(form -> form
                .loginPage("/login-petani")
                .loginProcessingUrl("/login-petani")
                .defaultSuccessUrl("/petani/dashboard", true)
                .failureHandler((request, response, exception) -> {
                    String errorMessage = "Username atau kata sandi salah.";
                    if (exception instanceof org.springframework.security.authentication.LockedException) {
                        errorMessage = exception.getMessage();
                    }
                    response.sendRedirect("/login-petani?error=true&errorMsg=" + java.net.URLEncoder.encode(errorMessage, java.nio.charset.StandardCharsets.UTF_8));
                })
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/petani/logout")
                .logoutSuccessUrl("/?logout=true")
                .permitAll()
            )
            .rememberMe(rm -> rm
                .key("sipangan_petani_secret_key_456")
                .rememberMeParameter("remember-me")
                .rememberMeCookieName("sipangan-petani-rm")
                .userDetailsService(userDetailsService())
                .tokenValiditySeconds(86400 * 30) // 30 days
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // ====== Public Chain ======
    @Bean
    @Order(3)
    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/register-petani", "/css/**", "/js/**", "/images/**", "/api/**").permitAll()
                .anyRequest().permitAll()
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
