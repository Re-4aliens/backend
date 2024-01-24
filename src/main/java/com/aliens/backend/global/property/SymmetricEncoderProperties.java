package com.aliens.backend.global.property;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class SymmetricEncoderProperties {

    @Value("${encode.symmetric.key}")
    private String secretKey;

    private SecretKeySpec encodeKey;

    @PostConstruct
    public void init() {
        byte[] byteKey = secretKey.getBytes(StandardCharsets.UTF_8);
        encodeKey = new SecretKeySpec(byteKey, "AES");
    }

    public SecretKeySpec getEncodeKey() {
        return encodeKey;
    }
}
