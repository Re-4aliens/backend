package com.aliens.backend.uploader;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class AwsS3Uploader {

    private final AmazonS3Client amazonS3Client;
    private final S3UploadProperties s3UploadProperties;
    private final LocalUploadProperties localUploadProperties;

    public AwsS3Uploader(final AmazonS3Client amazonS3Client, final S3UploadProperties s3UploadProperties, final LocalUploadProperties localUploadProperties) {
        this.amazonS3Client = amazonS3Client;
        this.s3UploadProperties = s3UploadProperties;
        this.localUploadProperties = localUploadProperties;
    }

    public String upload(MultipartFile multipartFile) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));

        return uploadToS3(uploadFile);
    }

    //로컬에 파일생성
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    private String uploadToS3(File uploadFile) {
        String fileName = new StringBuilder()
                .append(localUploadProperties.getUploadPath()).append("/")
                .append(UUID.randomUUID())
                .append(uploadFile.getName()).toString();

        String uploadImageUrl = putS3(uploadFile, fileName);  // S3로 업로드
        uploadFile.delete(); // 로컬 파일 삭제
        return uploadImageUrl;
    }

    // S3에 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(s3UploadProperties.getBucket(),
                fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(s3UploadProperties.getBucket(),fileName).toString();
    }

    public boolean delete(String fileName) {
        amazonS3Client.deleteObject(s3UploadProperties.getBucket(), fileName);
        return true;
    }
}