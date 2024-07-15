package com.example.bdMarketMaster.features.license.controller;

import com.example.bdMarketMaster.features.license.dao.LicenseRelationDAO;
import com.example.bdMarketMaster.features.license.services.LicenseRelationServices;
import com.example.bdMarketMaster.models.license.BusinessLicense;
import com.example.bdMarketMaster.models.license.LicenseRelation;
import com.example.bdMarketMaster.models.license.ProducerLicense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("license")
public class LicenseRelationController {

    @Autowired
    private LicenseRelationServices licenseRelationServices;

    @PostMapping("/producer/apply/{user_id}")
    public ResponseEntity<?> applyProducerLicense(@PathVariable Long user_id,
                                                  @RequestBody ProducerLicense producerLicense) {

        return licenseRelationServices.addProducerLicense(user_id, producerLicense);
    }

    @PostMapping("/business/apply/{user_id}")
    public ResponseEntity<?> applyBusinessLicense(@PathVariable Long user_id, @RequestBody BusinessLicense businessLicense) {

        return licenseRelationServices.addBusinessLicense(user_id, businessLicense);
    }

    @Autowired
    private LicenseRelationDAO relationDAO;

    @GetMapping("/id")
    public ResponseEntity<?> getAllLicenses() {
        return new ResponseEntity<>(relationDAO.findByUserId(2L), HttpStatus.OK);
    }
}
