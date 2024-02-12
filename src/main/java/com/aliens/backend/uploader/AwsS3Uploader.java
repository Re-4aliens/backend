package com.aliens.backend.uploader;

import com.aliens.backend.global.property.S3UploadProperties;
import com.aliens.backend.uploader.dto.S3File;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class AwsS3Uploader {

    private final AmazonS3Client amazonS3Client;
    private final S3UploadProperties s3UploadProperties;

    private static final String SUFFIX = ".png";

    public AwsS3Uploader(final AmazonS3Client amazonS3Client, final S3UploadProperties s3UploadProperties) {
        this.amazonS3Client = amazonS3Client;
        this.s3UploadProperties = s3UploadProperties;
    }

    public List<S3File> multiUpload(List<MultipartFile> files) {
        return files.stream().map(this::uploadToS3).toList();
    }

    public S3File singleUpload(MultipartFile file) {
        return uploadToS3(file);
    }

    private S3File uploadToS3(MultipartFile multipartFile) {
        String uuidName = UUID.randomUUID() + SUFFIX;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(MediaType.IMAGE_PNG_VALUE);
        metadata.setContentLength(multipartFile.getSize());
        try {
            amazonS3Client.putObject(
                    s3UploadProperties.getBucket(),
                    uuidName,
                    multipartFile.getInputStream(),
                    metadata);
        }catch (IOException e) {
            throw new RuntimeException("업로드 실패");
        }

        return new S3File(uuidName,amazonS3Client.getUrl(s3UploadProperties.getBucket(),uuidName).toString());
    }

    public boolean delete(String fileName) {
        amazonS3Client.deleteObject(s3UploadProperties.getBucket(), fileName);
        return true;
    }
}