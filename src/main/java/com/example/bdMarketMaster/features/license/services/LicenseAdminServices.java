package com.example.bdMarketMaster.features.license.services;

import com.example.bdMarketMaster.features.license.dao.BusinessLicenseDAO;
import com.example.bdMarketMaster.features.license.dao.ProducerLicenseDAO;
import com.example.bdMarketMaster.models.license.BusinessLicense;
import com.example.bdMarketMaster.models.license.ProducerLicense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LicenseAdminServices {

    @Autowired
    private BusinessLicenseDAO businessLicenseDAO;

    @Autowired
    private ProducerLicenseDAO producerLicenseDAO;

    public ResponseEntity<?> approveBusinessLicense(Long id) {
        Optional<BusinessLicense> optionalBusinessLicense = businessLicenseDAO.findById(id);
        if (optionalBusinessLicense.isEmpty()) {
            return new ResponseEntity<>("License not found.", HttpStatus.NOT_FOUND);
        }

        BusinessLicense businessLicense = optionalBusinessLicense.get();
        businessLicense.setActive(true);
        businessLicenseDAO.save(businessLicense);
        return new ResponseEntity<>("Business License approved.", HttpStatus.OK);
    }

    public ResponseEntity<?> approveProducerLicense(Long id) {
        Optional<ProducerLicense> optionalProducerLicense = producerLicenseDAO.findById(id);
        if (optionalProducerLicense.isEmpty()) {
            return new ResponseEntity<>("License not found.", HttpStatus.NOT_FOUND);
        }

        ProducerLicense producerLicense = optionalProducerLicense.get();
        producerLicense.setActive(true);
        producerLicenseDAO.save(producerLicense);
        return new ResponseEntity<>("Producer License approved.", HttpStatus.OK);
    }
}
