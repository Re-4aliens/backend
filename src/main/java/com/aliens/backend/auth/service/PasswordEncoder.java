package com.aliens.backend.auth.service;

import com.aliens.backend.global.error.MemberError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.EncodeProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class PasswordEncoder {

    private final EncodeProperties encodeProperties;

    public PasswordEncoder(final EncodeProperties encodeProperties) {
        this.encodeProperties = encodeProperties;
    }

    public String encrypt(String input) {
        try {
            KeySpec spec = new PBEKeySpec(input.toCharArray(), encodeProperties.getKey(), 85319, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RestApiException(MemberError.INVALID_PASSWORD);
        }
    }
}
