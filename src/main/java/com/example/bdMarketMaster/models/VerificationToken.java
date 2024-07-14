package com.example.bdMarketMaster.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = UserModel.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserModel userModel;

    private LocalDateTime expiryDate;

    public VerificationToken(String token, UserModel userModel, LocalDateTime expiryDate) {
        this.token = token;
        this.userModel = userModel;
        this.expiryDate = expiryDate;
    }

    public VerificationToken() {

    }
}
