package com.foodtrack.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final String defaultFailureUrl;

    public CustomAuthenticationFailureHandler(String defaultFailureUrl) {
        this.defaultFailureUrl = defaultFailureUrl;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Username atau kata sandi salah.";
        if (exception instanceof LockedException || (exception.getMessage() != null && exception.getMessage().contains("Tunggu"))) {
            errorMessage = exception.getMessage();
        }
        
        getRedirectStrategy().sendRedirect(request, response, defaultFailureUrl + "?errorMsg=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
    }
}
