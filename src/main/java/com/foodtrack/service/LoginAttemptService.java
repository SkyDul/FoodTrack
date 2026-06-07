package com.foodtrack.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;

@Service


/**
 * Kelas Service untuk LoginAttemptService.
 * Berisi logika bisnis dan bertindak sebagai penghubung antara Controller dan Repository.
 */
public class LoginAttemptService {
    private final int MAX_ATTEMPT = 3;
    private final int LOCK_TIME_DURATION_SECONDS = 30;

    private ConcurrentHashMap<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, LocalDateTime> lockTimeCache = new ConcurrentHashMap<>();

    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
        lockTimeCache.remove(key);
    }

    public void loginFailed(String key) {
        int attempts = attemptsCache.getOrDefault(key, 0);
        attempts++;
        attemptsCache.put(key, attempts);
        if (attempts >= MAX_ATTEMPT) {
            lockTimeCache.put(key, LocalDateTime.now());
        }
    }

    public boolean isBlocked(String key) {
        if (!lockTimeCache.containsKey(key)) {
            return false;
        }
        LocalDateTime lockTime = lockTimeCache.get(key);
        long secondsPassed = ChronoUnit.SECONDS.between(lockTime, LocalDateTime.now());
        if (secondsPassed >= LOCK_TIME_DURATION_SECONDS) {
            attemptsCache.remove(key);
            lockTimeCache.remove(key);
            return false;
        }
        return true;
    }
    
    public long getWaitTimeSeconds(String key) {
        if (!lockTimeCache.containsKey(key)) return 0;
        LocalDateTime lockTime = lockTimeCache.get(key);
        long secondsPassed = ChronoUnit.SECONDS.between(lockTime, LocalDateTime.now());
        return Math.max(0, LOCK_TIME_DURATION_SECONDS - secondsPassed);
    }
}

