package com.example.bdMarketMaster.features.auth.services;

import com.example.bdMarketMaster.features.auth.dao.AuthDAO;
import com.example.bdMarketMaster.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class AuthService {
    @Autowired
    private AuthDAO authDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;


    public ResponseEntity<?> signup(UserModel userModel) {
        try {
            if(userModel.getEmail() == null && userModel.getPhoneNumber() == null){
                return new ResponseEntity<>("Email or Phone number is required", HttpStatus.BAD_REQUEST);
            }
            userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
            authDAO.save(userModel);

            UserDetails userDetails = User.withUsername(userModel.getIdentity())
                    .password(userModel.getPassword())
                    .roles("USER")
                    .build();

            JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
            if (!userDetailsManager.userExists(userDetails.getUsername())) {
                userDetailsManager.createUser(userDetails);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (DataIntegrityViolationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("This credential already exists. If you forget password goto recover password.", HttpStatus.BAD_REQUEST);
        }
    }

    public UserModel getUserDetailsByIdentity(String identity) {
        return authDAO.findByIdentity(identity);
    }
}
