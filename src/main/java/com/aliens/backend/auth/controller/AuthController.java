package com.aliens.backend.auth.controller;

import com.aliens.backend.auth.service.AuthService;
import com.aliens.backend.auth.controller.dto.AuthToken;
import com.aliens.backend.auth.controller.dto.LoginRequest;
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
    public ResponseEntity<AuthToken> login(@RequestBody final LoginRequest loginRequest) {
        AuthToken authToken = authService.login(loginRequest);
        return ResponseEntity.ok(authToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody final AuthToken authToken) {
        String result = authService.logout(authToken);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/reissue")
    public ResponseEntity<AuthToken> reissue(@RequestBody final AuthToken authToken) {
        AuthToken newAuthToken = authService.reissue(authToken);
        return ResponseEntity.ok(newAuthToken);
    }
}