package com.example.bdMarketMaster.features.license.dao;

import com.example.bdMarketMaster.models.license.ProducerLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducerLicenseDAO extends JpaRepository<ProducerLicense, Long> {

}
