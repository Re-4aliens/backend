package com.aliens.backend.email.controller;

import com.aliens.backend.email.service.EmailService;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.EmailSuccess;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


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
    public SuccessResponse<String> sendAuthenticationEmail(@RequestParam("email") String email) {

        return SuccessResponse.of(
                EmailSuccess.SEND_EMAIL_SUCCESS,
                emailService.sendAuthenticationEmail(email)
        );
    }

    @GetMapping("/emails/confirm")
    public ModelAndView authenticateEmail(@RequestParam("token") String token) {
        emailService.authenticateEmail(token);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("emailVerificationComplete");
        return modelAndView;
    }

    @GetMapping("/emails")
    public SuccessResponse<String> checkEmailAuthenticated(@RequestParam("email") String email) {

        return SuccessResponse.of(
                EmailSuccess.EMAIL_AUTHENTICATE_SUCCESS,
                emailService.checkEmailAuthenticated(email)
        );
    }
}
