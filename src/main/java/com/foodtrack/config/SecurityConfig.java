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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AdminRepository adminRepository;
    private final PetaniRepository petaniRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ====== Admin Security Chain ======
    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/admin/**", "/logout")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/login").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
            )
            .formLogin(form -> form
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .defaultSuccessUrl("/admin/dashboard", true)
                .failureUrl("/admin/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/admin/login?logout=true")
                .permitAll()
            )
            .userDetailsService(adminUserDetailsService());

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
                .failureUrl("/login-petani?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/petani/logout")
                .logoutSuccessUrl("/login-petani?logout=true")
                .permitAll()
            )
            .userDetailsService(petaniUserDetailsService());

        return http.build();
    }

    // ====== Public Chain (Landing, API, Static) ======
    @Bean
    @Order(3)
    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/api/**").permitAll()
                .anyRequest().permitAll()
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // ====== UserDetailsService for Admin ======
    @Bean
    public UserDetailsService adminUserDetailsService() {
        return username -> {
            Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin tidak ditemukan: " + username));
            return User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .roles("ADMIN")
                .build();
        };
    }

    // ====== UserDetailsService for Petani ======
    public UserDetailsService petaniUserDetailsService() {
        return username -> {
            Petani petani = petaniRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Petani tidak ditemukan: " + username));
            return User.builder()
                .username(petani.getUsername())
                .password(petani.getPassword())
                .roles("PETANI")
                .build();
        };
    }
}
