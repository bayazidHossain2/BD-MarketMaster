package com.example.bdMarketMaster.models.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("product_name")
    String productName;
    String category;
    Integer quantity;
    String description;
    @JsonProperty("production_date")
    String productionDate;
    @JsonProperty("expire_date")
    String expireDate;
}
