package com.aliens.backend.auth.service;

import com.aliens.backend.auth.controller.dto.AuthToken;
import com.aliens.backend.auth.controller.dto.LoginRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public static final String LOGOUT_SUCCESS = "로그아웃 되었습니다.";

    public AuthToken login(LoginRequest loginRequest) {
        return new AuthToken("accessToken", "refreshToken");
    }

    public String logout(final AuthToken authToken) {
        return LOGOUT_SUCCESS;
    }

    public AuthToken reissue(final AuthToken authToken) {
        return new AuthToken("accessToken", "refreshToken");
    }
}
