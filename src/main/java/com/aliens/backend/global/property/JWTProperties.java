package com.aliens.backend.global.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class JWTProperties {
    @Value("${jwt.secret_key}")
    private String secretKey;

    @Value("${jwt.access_token_valid_time}")
    private Long accessTokenValidTime;

    @Value("${jwt.refresh_token_valid_time}")
    private Long refreshTokenValidTime ;


    public byte[] getBytesSecretKey() {
        return secretKey.getBytes(StandardCharsets.UTF_8);
    }

    public long getRefreshTokenValidTime() {
        return refreshTokenValidTime;
    }

    public long getAccessTokenValidTime() {
        return accessTokenValidTime;
    }

    public void setAccessTokenValidTime(final Long time) {
        accessTokenValidTime = time;
    }

    public void setRefreshTokenValidTime(final Long time) {
        refreshTokenValidTime = time;
    }
}

