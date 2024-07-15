package com.example.bdMarketMaster.features.license.dao;

import com.example.bdMarketMaster.models.license.BusinessLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessLicenseDAO extends JpaRepository<BusinessLicense, Long> {
}
