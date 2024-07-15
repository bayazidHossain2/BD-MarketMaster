package com.example.bdMarketMaster.models.license;

import com.example.bdMarketMaster.models.UserModel;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class LicenseRelation{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = UserModel.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserModel userModel;

    @OneToOne(targetEntity = BusinessLicense.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "business_license_id")
    private BusinessLicense businessLicense;

    @OneToOne(targetEntity = ProducerLicense.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true, name = "producer_license_id")
    private ProducerLicense producerLicense;
}
