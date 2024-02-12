package com.aliens.backend.member.sevice;

import com.aliens.backend.global.response.error.CommonError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.SymmetricEncoderProperties;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class SymmetricKeyEncoder {

    private final SymmetricEncoderProperties symmetricEncoderProperties;

    public SymmetricKeyEncoder(final SymmetricEncoderProperties symmetricEncoderProperties) {
        this.symmetricEncoderProperties = symmetricEncoderProperties;
    }

    public String encrypt(String plaintext) {
        try {
            Cipher cipher = Cipher.getInstance(symmetricEncoderProperties.getChipper());
            cipher.init(Cipher.ENCRYPT_MODE, symmetricEncoderProperties.getEncodeKey());
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RestApiException(CommonError.ENCODE_ERROR);
        }
    }

    public String decrypt(String ciphertext) {
        try {
            Cipher cipher = Cipher.getInstance(symmetricEncoderProperties.getChipper());
            cipher.init(Cipher.DECRYPT_MODE, symmetricEncoderProperties.getEncodeKey());
            byte[] decodedBytes = Base64.getDecoder().decode(ciphertext);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RestApiException(CommonError.ENCODE_ERROR);
        }
    }
}
