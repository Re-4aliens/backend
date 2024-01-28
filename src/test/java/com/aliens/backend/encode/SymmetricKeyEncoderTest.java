package com.aliens.backend.encode;

import com.aliens.backend.global.encode.SymmetricKeyEncoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SymmetricKeyEncoderTest {

    @Autowired
    private SymmetricKeyEncoder symmetricKeyEncoder;

    @Test
    @DisplayName("문자열 암호화 성공")
    void encryptTest() throws Exception {
        //Given
        String givenInput = "testText";

        //When
        String result = symmetricKeyEncoder.encrypt(givenInput);

        //Then
        Assertions.assertNotEquals(result, givenInput);
    }

    @Test
    @DisplayName("문자열 복호화 성공")
    void decryptTest() throws Exception {
        //Given
        String givenInput = "testText";
        String code = symmetricKeyEncoder.encrypt(givenInput);

        //When
        String result = symmetricKeyEncoder.decrypt(code);

        //Then
        Assertions.assertEquals(result, givenInput);
    }
}
