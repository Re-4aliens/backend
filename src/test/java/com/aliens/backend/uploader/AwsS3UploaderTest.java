package com.aliens.backend.uploader;

import io.findify.s3mock.S3Mock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

@Import(AwsS3MockConfig.class)
@SpringBootTest
class AwsS3UploaderTest {

    @Autowired
    private S3Mock s3Mock;

    @Autowired
    private AwsS3Uploader awsS3Uploader;

    @AfterEach
    public void tearDown() {
        s3Mock.stop();
    }

    @Test
    @DisplayName("S3 파일 업로드 테스트")
    void uploadTest() throws IOException {
        // Given
        String path = "test.png";
        String contentType = "image/png";
        MockMultipartFile file = new MockMultipartFile("test", path, contentType, "test".getBytes());

        // When
        String urlPath = awsS3Uploader.upload(file);

        // Then
        Assertions.assertThat(urlPath).contains(path);
    }

    @Test
    @DisplayName("S3 파일 삭제 테스트")
    void deleteTest() {
        // Given
        String path = "test.png";

        // When
        boolean result = awsS3Uploader.delete(path);

        // Then
        org.junit.jupiter.api.Assertions.assertTrue(result);
    }
}