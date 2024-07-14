package com.example.bdMarketMaster.features.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenCleanupScheduler {

    @Autowired
    private VerificationTokenService tokenService;

    @Scheduled(fixedRate = 60000) // Run every 1 minute
    public void deleteExpiredTokens() {
        tokenService.deleteExpiredTokens();
    }
}