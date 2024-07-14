package com.example.bdMarketMaster.features.auth.services;


import com.example.bdMarketMaster.features.auth.dao.AuthDAO;
import com.example.bdMarketMaster.features.auth.dao.VarificationTokenDAO;
import com.example.bdMarketMaster.models.UserModel;
import com.example.bdMarketMaster.models.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService {
    @Autowired
    private VarificationTokenDAO verificationTokenDAO;

    @Autowired
    private AuthDAO authDAO;

    public VerificationToken createVerificationToken(UserModel userModel) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(50);
        VerificationToken verificationToken = new VerificationToken(token, userModel, expiryDate);
        return verificationTokenDAO.save(verificationToken);
    }

    public Optional<VerificationToken> getVerificationToken(String token) {
        return verificationTokenDAO.findByToken(token);
    }

    public void deleteExpiredTokens() {
        verificationTokenDAO.deleteByExpiryDateBefore(LocalDateTime.now());
    }

    public void verifyToken(String token) throws Exception {
        System.out.println("in valid token");
//        Optional<VerificationToken> verificationTokenOpt = getVerificationToken(token);
        Optional<VerificationToken> verificationTokenOpt = verificationTokenDAO.findByToken(token);

        System.out.println("Token found: " + verificationTokenOpt.get().getToken());
        if (verificationTokenOpt.isEmpty()) {
            throw new Exception("Invalid token");
        }

        VerificationToken verificationToken = verificationTokenOpt.get();
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new Exception("Token has expired");
        }

        UserModel userModel = verificationToken.getUserModel();
        userModel.setVerifyTryCount(-1); // or any other logic to enable the user
        authDAO.save(userModel);

        verificationTokenDAO.delete(verificationToken);
    }
}
