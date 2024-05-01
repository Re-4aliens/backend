package com.aliens.backend.uploader;

import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.S3UploadProperties;
import com.aliens.backend.global.response.error.BoardError;
import com.aliens.backend.uploader.dto.S3File;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class AwsS3Uploader {

    private final AmazonS3Client amazonS3Client;
    private final S3UploadProperties s3UploadProperties;

    private static final String SUFFIX = ".png";
    private static final int MAX_UPLOADS = 2;
    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024; // 10 MB


    public AwsS3Uploader(final AmazonS3Client amazonS3Client, final S3UploadProperties s3UploadProperties) {
        this.amazonS3Client = amazonS3Client;
        this.s3UploadProperties = s3UploadProperties;
    }

    public List<S3File> multiUpload(List<MultipartFile> files) {

        checkFileSize(files);
        return files.stream().map(this::uploadToS3).toList();
    }

    private void checkFileSize(final List<MultipartFile> files) {
        if(files.size() > MAX_UPLOADS) {
            throw new RestApiException(BoardError.POST_IMAGE_ERROR);
        }
    }

    public S3File singleUpload(MultipartFile file) {
        return uploadToS3(file);
    }

    private S3File uploadToS3(MultipartFile multipartFile) {
        checkFileSize(multipartFile);
        checkFilesImage(multipartFile);

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
            throw new RestApiException(BoardError.POST_IMAGE_ERROR);
        }

        return new S3File(uuidName,amazonS3Client.getUrl(s3UploadProperties.getBucket(),uuidName).toString());
    }

    private void checkFilesImage(final MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        if(contentType == null
                || (!contentType.equals(MediaType.IMAGE_JPEG_VALUE)
                && !contentType.equals(MediaType.IMAGE_PNG_VALUE))) {
            throw new RestApiException(BoardError.POST_IMAGE_ERROR);
        }
    }

    private void checkFileSize(final MultipartFile multipartFile) {
        if (multipartFile.getSize() > MAX_IMAGE_SIZE) {
            throw new RestApiException(BoardError.POST_IMAGE_ERROR);
        }
    }

    public boolean delete(String fileName) {
        amazonS3Client.deleteObject(s3UploadProperties.getBucket(), fileName);
        return true;
    }
}