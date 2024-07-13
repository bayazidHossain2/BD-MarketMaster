package com.example.bdMarketMaster.models.helper;

import com.example.bdMarketMaster.models.UserModel;
import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {
    private String jwtToken;
    private UserModel userModel;
    private List<String> roles;

    public LoginResponse(UserModel userModel, List<String> roles, String jwtToken) {
        this.jwtToken = jwtToken;
        this.userModel = userModel;
        this.roles = roles;
    }
}
