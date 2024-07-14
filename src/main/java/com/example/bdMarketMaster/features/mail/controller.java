package com.example.bdMarketMaster.features.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mail")
public class controller {

    @Autowired
    private MailService mailService;

//    @Autowired
//    private MailSender mailSender;


    @GetMapping("/send")
    public ResponseEntity<?> sendSimpleEmail() {
        String email = "bayazid204@gmail.com";
        String subject = "Test Mail";
        String body = "This is a simple mail message";


        mailService.sendSimpleEmail(email, subject, body);
        return new ResponseEntity<>("send success", HttpStatus.OK);
    }
}
