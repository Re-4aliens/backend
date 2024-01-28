package com.aliens.backend.uploader;

import com.aliens.backend.uploader.dto.S3File;
import io.findify.s3mock.S3Mock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    void uploadTest() {
        // Given
        String path = "test.png";
        String contentType = "image/png";
        MockMultipartFile file = new MockMultipartFile("test", path, contentType, "test".getBytes());

        List<MultipartFile> request =  List.of(file,file,file);

        // When
        List<S3File> S3Files = awsS3Uploader.upload(request);

        // Then
        Assertions.assertThat(S3Files).isNotNull();
    }

    @Test
    @DisplayName("S3 파일 삭제 테스트")
    void deleteTest() {
        // Given
        String fileName = "test.png";

        // When
        boolean result = awsS3Uploader.delete(fileName);

        // Then
        org.junit.jupiter.api.Assertions.assertTrue(result);
    }
}