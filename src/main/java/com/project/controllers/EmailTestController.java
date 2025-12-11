package com.project.controllers;

import com.project.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@RestController
@RequestMapping("/api/test")
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/email")
    public String testEmail(@RequestParam String to) {
        String subject = "Test Email";
        String body = "Hello! This is a test email sent from EzeeRide backend.";

        emailService.sendEmail(to, subject, body);

        return "Email sent to: " + to;
    }
}
