package com.aliens.backend.email.controller;

import com.aliens.backend.email.service.EmailService;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.EmailSuccess;
import org.springframework.web.bind.annotation.*;


@RestController
public class EmailController {
    private final EmailService emailService;

    public EmailController(final EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/members/exist")
    public SuccessResponse<String> duplicateCheck(@RequestParam("email") String email) {

        return SuccessResponse.of(
                EmailSuccess.DUPLICATE_CHECK_SUCCESS,
                emailService.duplicateCheck(email)
        );
    }

    @PostMapping("/emails/verification/send")
    public SuccessResponse<String> sendAuthenticationEmail(@RequestBody String email) throws Exception {

        return SuccessResponse.of(
                EmailSuccess.SEND_EMAIL_SUCCESS,
                emailService.sendAuthenticationEmail(email)
        );
    }

    @GetMapping("/emails/confirm")
    public SuccessResponse<String> authenticateEmail(@RequestParam("token") String token) throws Exception {

        return SuccessResponse.of(
                EmailSuccess.EMAIL_AUTHENTICATE_SUCCESS,
                emailService.authenticateEmail(token)
        );
    }

    @GetMapping("/emails")
    public SuccessResponse<String> checkEmailAuthenticated(@RequestParam("email") String email) throws Exception {

        return SuccessResponse.of(
                EmailSuccess.EMAIL_AUTHENTICATE_SUCCESS,
                emailService.checkEmailAuthenticated(email)
        );
    }
}
