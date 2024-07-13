package com.example.bdMarketMaster.features.auth.dao;

import com.example.bdMarketMaster.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthDAO  extends JpaRepository<UserModel, Long> {

    UserModel findByIdentity(String identity);
}
