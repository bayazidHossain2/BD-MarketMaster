package com.example.bdMarketMaster.features.license.controller;

import com.example.bdMarketMaster.features.license.services.LicenseAdminServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class LicenseAdminController {

    @Autowired
    private LicenseAdminServices licenseAdminServices;

    @GetMapping("/businesslicense/approve/{id}")
    public ResponseEntity<?> approveBusinessLicense(@PathVariable Long id) {
        return licenseAdminServices.approveBusinessLicense(id);
    }

    @GetMapping("/producerlicense/approve/{id}")
    public ResponseEntity<?> approveProducerLicense(@PathVariable Long id) {
        return licenseAdminServices.approveProducerLicense(id);
    }

    @GetMapping("/hi")
    public String hi() {
        return "Hi";
    }
}
