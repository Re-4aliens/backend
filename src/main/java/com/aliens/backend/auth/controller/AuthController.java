package com.aliens.backend.auth.controller;

import com.aliens.backend.auth.service.AuthService;
import com.aliens.backend.auth.controller.dto.AuthToken;
import com.aliens.backend.auth.controller.dto.LoginRequest;
import com.aliens.backend.global.response.success.AuthSuccess;
import com.aliens.backend.global.response.SuccessResponse;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/authentication")
@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public SuccessResponse<AuthToken> login(@RequestBody final LoginRequest loginRequest) {

        return SuccessResponse.of(
                AuthSuccess.GENERATE_TOKEN_SUCCESS,
                authService.login(loginRequest)
        );
    }

    @PostMapping("/logout")
    public SuccessResponse<String> logout(@RequestBody final AuthToken authToken) {

        return SuccessResponse.of(
                AuthSuccess.LOGOUT_SUCCESS,
                authService.logout(authToken)
        );
    }

    @PostMapping("/reissue")
    public SuccessResponse<AuthToken> reissue(@RequestBody final AuthToken authToken) {

        return SuccessResponse.of(
                AuthSuccess.REISSUE_TOKEN_SUCCESS,
                authService.reissue(authToken)
        );
    }
}