package com.aliens.backend.encode;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class SymmetricKeyEncoder {

    private final EncoderProperties encoderProperties;

    public SymmetricKeyEncoder(final EncoderProperties encoderProperties) {
        this.encoderProperties = encoderProperties;
    }

    public String encrypt(String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, encoderProperties.getEncodeKey());
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String ciphertext) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, encoderProperties.getEncodeKey());
        byte[] decodedBytes = Base64.getDecoder().decode(ciphertext);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
