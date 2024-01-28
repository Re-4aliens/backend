package com.aliens.backend.email.controller;

import com.aliens.backend.email.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
public class EmailController {
    private final EmailService emailService;

    public EmailController(final EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/members/exist")
    public ResponseEntity<Map<String, String>> duplicateCheck(@RequestParam("email") String email) {
        String result = emailService.duplicateCheck(email);
        Map<String, String> response = Collections.singletonMap("response", result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/emails/verification/send")
    public ResponseEntity<Map<String, String>> sendAuthenticationEmail(@RequestBody String email) throws Exception {
        String result = emailService.sendAuthenticationEmail(email);
        Map<String, String> response = Collections.singletonMap("response", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/emails/confirm")
    public ResponseEntity<Map<String, String>> authenticateEmail(@RequestParam("token") String token) throws Exception {
        String result = emailService.authenticateEmail(token);
        Map<String, String> response = Collections.singletonMap("response", result);
        return ResponseEntity.ok(response);
    }
}
