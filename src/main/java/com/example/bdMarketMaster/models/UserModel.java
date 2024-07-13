package com.example.bdMarketMaster.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users_details")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true, unique = true)
    private String email;
    @Column(nullable = true, unique = true)
    private String phoneNumber;
    private String username;
    private String dob;
    @Column(nullable = false, unique = true)
    private String identity; // nid or birth certificate;
    private String password;
}
