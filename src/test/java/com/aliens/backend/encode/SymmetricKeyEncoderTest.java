package com.aliens.backend.encode;

import com.aliens.backend.global.BaseIntegrationTest;
import com.aliens.backend.member.sevice.SymmetricKeyEncoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SymmetricKeyEncoderTest extends BaseIntegrationTest {

    @Autowired
    private SymmetricKeyEncoder symmetricKeyEncoder;

    @Test
    @DisplayName("문자열 암호화 성공")
    void encryptTest() {
        //Given
        String givenInput = "testText";

        //When
        String result = symmetricKeyEncoder.encrypt(givenInput);

        //Then
        Assertions.assertNotEquals(result, givenInput);
    }

    @Test
    @DisplayName("문자열 복호화 성공")
    void decryptTest() {
        //Given
        String givenInput = "testText";
        String code = symmetricKeyEncoder.encrypt(givenInput);

        //When
        String result = symmetricKeyEncoder.decrypt(code);

        //Then
        Assertions.assertEquals(result, givenInput);
    }
}
