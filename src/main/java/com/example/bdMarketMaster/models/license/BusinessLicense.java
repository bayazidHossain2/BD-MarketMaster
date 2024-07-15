package com.example.bdMarketMaster.models.license;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class BusinessLicense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("initial_investment")
    private double initialInvestment;
    private double capital;
    @JsonProperty("estimated_profit")
    private double estimatedProfit;
    @Column(length = 2000)
    private String description;
    @JsonProperty("is_active")
    private boolean isActive = false;
}
