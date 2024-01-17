package com.aliens.backend.auth.controller.dto;

public record AuthToken(String accessToken, String refreshToken) {
}
