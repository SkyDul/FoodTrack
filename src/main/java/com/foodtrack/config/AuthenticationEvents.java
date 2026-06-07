package com.foodtrack.config;

import com.foodtrack.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor


/**
 * Kelas konfigurasi untuk AuthenticationEvents di dalam spring boot.
 */
public class AuthenticationEvents {

    private final LoginAttemptService loginAttemptService;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        loginAttemptService.loginSucceeded(success.getAuthentication().getName());
    }

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent failure) {
        loginAttemptService.loginFailed(failure.getAuthentication().getName());
    }
}

