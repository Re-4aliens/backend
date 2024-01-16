package com.aliens.backend.auth.service;

import com.aliens.backend.auth.controller.dto.AuthToken;
import com.aliens.backend.auth.controller.dto.LoginRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public AuthToken login(LoginRequest loginRequest) {
        return new AuthToken("accessToken", "refreshToken");
    }
}
