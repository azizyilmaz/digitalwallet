package com.aziz.digitalwallet.service.application;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class TokenRotationService {
    private final TokenService tokenService;

    // run every hour at :00
    @Scheduled(cron = "0 0 * * * *")
    public void rotateOldTokens() {
        Instant cutoff = Instant.now().minus(1, ChronoUnit.HOURS);
        var oldTokens = tokenService.findActiveOlderThan(cutoff);
        for (var t : oldTokens) {
            tokenService.rotateToken(t);
        }
    }
}