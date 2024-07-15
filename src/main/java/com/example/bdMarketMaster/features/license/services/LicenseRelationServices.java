package com.example.bdMarketMaster.features.license.services;

import com.example.bdMarketMaster.features.auth.dao.AuthDAO;
import com.example.bdMarketMaster.features.license.dao.BusinessLicenseDAO;
import com.example.bdMarketMaster.features.license.dao.LicenseRelationDAO;
import com.example.bdMarketMaster.features.license.dao.ProducerLicenseDAO;
import com.example.bdMarketMaster.models.UserModel;
import com.example.bdMarketMaster.models.license.BusinessLicense;
import com.example.bdMarketMaster.models.license.LicenseRelation;
import com.example.bdMarketMaster.models.license.ProducerLicense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LicenseRelationServices {

    @Autowired
    private LicenseRelationDAO licenseRelationDAO;

    @Autowired
    private ProducerLicenseDAO producerLicenseDAO;

    @Autowired
    private AuthDAO authDAO;
    @Autowired
    private BusinessLicenseDAO businessLicenseDAO;


    public ResponseEntity<?> addProducerLicense(Long userId, ProducerLicense producerLicense) {

        try {
            Optional<UserModel> optionalUserModel = authDAO.findById(userId);
            if (optionalUserModel.isPresent()) {
                producerLicenseDAO.save(producerLicense);
                LicenseRelation licenseRelation = new LicenseRelation();
                licenseRelation.setUserModel(optionalUserModel.get());
                licenseRelation.setProducerLicense(producerLicense);
                licenseRelationDAO.save(licenseRelation);
            }else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>("Something went wrong : " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Application created successfully", HttpStatus.OK);
    }

    public ResponseEntity<?> addBusinessLicense(Long userId, BusinessLicense businessLicense) {
        try {
            Optional<UserModel> optionalUserModel = authDAO.findById(userId);
            if (optionalUserModel.isEmpty()) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            Optional<LicenseRelation> optionalLicenseRelation = licenseRelationDAO.findByUserId(userId);
            LicenseRelation licenseRelation = new LicenseRelation();
            if(optionalUserModel.isPresent()){
                licenseRelation = optionalLicenseRelation.get();
            }
            if(licenseRelation.getBusinessLicense() != null){
                return new ResponseEntity<>("Business license already exists", HttpStatus.CONFLICT);
            }
            licenseRelation.setUserModel(optionalUserModel.get());
            licenseRelation.setBusinessLicense(businessLicense);
            businessLicenseDAO.save(businessLicense);
            licenseRelationDAO.save(licenseRelation);
        }catch (Exception e){
            return new ResponseEntity<>("Something went wrong : " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Application created successfully", HttpStatus.OK);
    }

    public boolean hasLicenseRelation(Long userId, String licenseType) {
        Optional<LicenseRelation> optionalLicenseRelation = licenseRelationDAO.findByUserId(userId);
        if(optionalLicenseRelation.isEmpty()){
            return false;
        }
        LicenseRelation licenseRelation = optionalLicenseRelation.get();
        if(licenseType.equals("PRODUCER") && licenseRelation.getProducerLicense() != null){
            return true;
        }
        else if(licenseType.equals("BUSINESS") && licenseRelation.getBusinessLicense() != null){
            return true;
        }

        return false;
    }
}
