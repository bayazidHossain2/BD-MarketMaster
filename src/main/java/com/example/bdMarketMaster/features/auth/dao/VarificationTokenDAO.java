package com.example.bdMarketMaster.features.auth.dao;

import com.example.bdMarketMaster.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VarificationTokenDAO extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    void deleteByExpiryDateBefore(LocalDateTime now);
}
