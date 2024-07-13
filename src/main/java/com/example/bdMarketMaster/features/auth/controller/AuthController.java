package com.example.bdMarketMaster.features.auth.controller;

import com.example.bdMarketMaster.features.auth.services.AuthService;
import com.example.bdMarketMaster.models.UserModel;
import com.example.bdMarketMaster.models.helper.LoginRequest;
import com.example.bdMarketMaster.models.helper.LoginResponse;
import com.example.bdMarketMaster.security.jwt.AuthenticationService;
import com.example.bdMarketMaster.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("public")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("signup")
    public ResponseEntity<?> signupUser(@RequestBody UserModel userModel) {
        return authService.signup(userModel);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try{
            authentication = authenticationService
                    .authenticate(loginRequest.getIdentity(), loginRequest.getPassword());
        }catch(AuthenticationException exception){
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status",false);
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserModel userModel = authService.getUserDetailsByIdentity(loginRequest.getIdentity());
        userModel.setPassword("@very -_- hard@");

        LoginResponse response = new LoginResponse(userModel, roles, jwtToken);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("id/{identity}")
//    public ResponseEntity<?> getUser(@PathVariable String identity){
//        return new ResponseEntity<>( authService.getUserDetailsByIdentity(identity), HttpStatus.OK);
//    }
}
