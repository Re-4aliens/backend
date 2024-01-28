package com.aliens.backend.global.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class PasswordEncodeProperties {

    @Value("${encode.key}")
    private String secretKey;

    public byte[] getKey() {
        return secretKey.getBytes(StandardCharsets.UTF_8);
    }
}
