package com.aliens.backend.auth.controller;

import com.aliens.backend.auth.service.AuthService;
import com.aliens.backend.auth.controller.dto.AuthToken;
import com.aliens.backend.auth.controller.dto.LoginRequest;
import com.aliens.backend.global.success.AuthSuccessCode;
import com.aliens.backend.global.success.SuccessResponse;
import com.aliens.backend.global.success.SuccessResponseWithoutResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/authentication")
@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody final LoginRequest loginRequest) {
        return SuccessResponse.toResponseEntity(AuthSuccessCode.GENERATE_AUTHTOKEN_SUCCESS, authService.login(loginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody final AuthToken authToken) {
        return SuccessResponseWithoutResult.toResponseEntity(AuthSuccessCode.LOGOUT_SUCCESS);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody final AuthToken authToken) {
        return SuccessResponse.toResponseEntity(AuthSuccessCode.REISSUE_AUTHTOKEN_SUCCESS, authService.reissue(authToken));
    }
}