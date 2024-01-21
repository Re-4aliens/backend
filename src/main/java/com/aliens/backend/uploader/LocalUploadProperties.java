package com.aliens.backend.uploader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LocalUploadProperties {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Value("${spring.servlet.multipart.enabled}")
    private boolean available;

    @Value("${spring.servlet.multipart.max-request-size}")
    private String maxRequestSize;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    public String getUploadPath() {
        return uploadPath;
    }
}
