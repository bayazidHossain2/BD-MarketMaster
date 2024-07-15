package com.example.bdMarketMaster.models.license;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProducerLicense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("amount_of_land")
    private double amountOfLand;
    @Column(length = 2000)
    private String description;
    @JsonProperty("estimated_profit_per_year")
    private double estimatedProfitPerYear;
    @JsonProperty("is_active")
    private boolean isActive = false;
}
