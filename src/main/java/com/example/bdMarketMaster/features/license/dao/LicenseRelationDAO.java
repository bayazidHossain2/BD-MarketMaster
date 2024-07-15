package com.example.bdMarketMaster.features.license.dao;

import com.example.bdMarketMaster.models.license.LicenseRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LicenseRelationDAO extends JpaRepository<LicenseRelation, Long> {

    // Custom query to find LicenseRelation by user_id
    @Query("SELECT lr FROM LicenseRelation lr WHERE lr.userModel.id = :userId")
    Optional<LicenseRelation> findByUserId(@Param("userId") Long userId);
}
