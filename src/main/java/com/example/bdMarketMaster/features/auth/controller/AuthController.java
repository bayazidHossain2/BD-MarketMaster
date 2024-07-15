package com.example.bdMarketMaster.features.auth.controller;

import com.example.bdMarketMaster.features.auth.dao.VarificationTokenDAO;
import com.example.bdMarketMaster.features.auth.services.AuthService;
import com.example.bdMarketMaster.features.auth.services.VerificationTokenService;
import com.example.bdMarketMaster.features.mail.MailService;
import com.example.bdMarketMaster.models.UserModel;
import com.example.bdMarketMaster.models.helper.LoginRequest;
import com.example.bdMarketMaster.models.helper.LoginResponse;
import com.example.bdMarketMaster.security.jwt.AuthenticationService;
import com.example.bdMarketMaster.security.jwt.JwtUtils;
import jakarta.mail.MessagingException;
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

    @Autowired
    private VerificationTokenService verificationTokenService;

    @PostMapping("signup")
    public ResponseEntity<?> signupUser(@RequestBody UserModel userModel) {
        return authService.signup(userModel);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) throws MessagingException {
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
        if(userModel.getVerifyTryCount() != -1){
            return new ResponseEntity<>("Account is not verified", HttpStatus.FORBIDDEN);
        }

        LoginResponse response = new LoginResponse(userModel, roles, jwtToken);
//        mailService.sendWelcomeEmail(userModel.getEmail(), userModel.getUsername());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/verify/{token}")
    public ResponseEntity<?> verifyUser(@PathVariable String token) {
        System.out.println("- - "+token);
        try {
            verificationTokenService.verifyToken(token);
        } catch (Exception e) {
            return new ResponseEntity<>(getVerificationMessage(false, e.getMessage()),
                    HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(getVerificationMessage(true, ""), HttpStatus.OK);
    }

//    @GetMapping("/id")
//    public ResponseEntity<?> getUserFF(){
//        return new ResponseEntity<>(
//                ""
//                , HttpStatus.OK);
////        return new ResponseEntity<>(varificationTokenDAO.findAll(), HttpStatus.OK);
//    }

    String getVerificationMessage(boolean isVerify, String message){
        if(isVerify){
            return "<!DOCTYPE html>\n" +
                    "<html xmlns:th=\"http://www.thymeleaf.org\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Email Verification</title>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: Arial, sans-serif;\n" +
                    "            background-color: #f7f7f7;\n" +
                    "            display: flex;\n" +
                    "            justify-content: center;\n" +
                    "            align-items: center;\n" +
                    "            height: 100vh;\n" +
                    "            margin: 0;\n" +
                    "        }\n" +
                    "        .container {\n" +
                    "            text-align: center;\n" +
                    "            background-color: #fff;\n" +
                    "            border-radius: 8px;\n" +
                    "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                    "            padding: 30px;\n" +
                    "            max-width: 400px;\n" +
                    "            width: 100%;\n" +
                    "        }\n" +
                    "        .card {\n" +
                    "            margin: 20px;\n" +
                    "        }\n" +
                    "        h1 {\n" +
                    "            color: #4CAF50;\n" +
                    "            font-size: 24px;\n" +
                    "            margin-bottom: 20px;\n" +
                    "        }\n" +
                    "        p {\n" +
                    "            color: #333;\n" +
                    "            font-size: 18px;\n" +
                    "            margin-bottom: 20px;\n" +
                    "        }\n" +
                    "        .btn {\n" +
                    "            display: inline-block;\n" +
                    "            background-color: #4CAF50;\n" +
                    "            color: white;\n" +
                    "            padding: 10px 20px;\n" +
                    "            text-decoration: none;\n" +
                    "            border-radius: 5px;\n" +
                    "        }\n" +
                    "        .btn:hover {\n" +
                    "            background-color: #45a049;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <div class=\"card\">\n" +
                    "            <h1>BD Market Master Email Verification Success</h1>\n" +
                    "            <p>Your email has been successfully verified!</p>\n" +
                    "            <a href=\"/\" class=\"btn\">Go to Login Page</a>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";
        }else{
            return ("<!DOCTYPE html>\n" +
                    "<html xmlns:th=\"http://www.thymeleaf.org\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Email Verification</title>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: Arial, sans-serif;\n" +
                    "            background-color: #f7f7f7;\n" +
                    "            display: flex;\n" +
                    "            justify-content: center;\n" +
                    "            align-items: center;\n" +
                    "            height: 100vh;\n" +
                    "            margin: 0;\n" +
                    "        }\n" +
                    "        .container {\n" +
                    "            text-align: center;\n" +
                    "            background-color: #fff;\n" +
                    "            border-radius: 8px;\n" +
                    "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                    "            padding: 30px;\n" +
                    "            max-width: 400px;\n" +
                    "            width: 100%;\n" +
                    "        }\n" +
                    "        .card {\n" +
                    "            margin: 20px;\n" +
                    "        }\n" +
                    "        h1 {\n" +
                    "            color: #FF0000;\n" +
                    "            font-size: 24px;\n" +
                    "            margin-bottom: 20px;\n" +
                    "        }\n" +
                    "        p {\n" +
                    "            color: #333;\n" +
                    "            font-size: 18px;\n" +
                    "            margin-bottom: 20px;\n" +
                    "        }\n" +
                    "        .btn {\n" +
                    "            display: inline-block;\n" +
                    "            background-color: #4CAF50;\n" +
                    "            color: white;\n" +
                    "            padding: 10px 20px;\n" +
                    "            text-decoration: none;\n" +
                    "            border-radius: 5px;\n" +
                    "        }\n" +
                    "        .btn:hover {\n" +
                    "            background-color: #45a049;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <div class=\"card\">\n" +
                    "            <h1>BD Market Master Email Verification Failed</h1>\n" +
                    "            <p>Invalid or expired verification link.</p>\n" +
                    "            <p>[message]</p>\n" +
                    "            <a href=\"/welcome\" class=\"btn\">Try Again</a>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>").replace("[message]", message);
        }
    }
}
