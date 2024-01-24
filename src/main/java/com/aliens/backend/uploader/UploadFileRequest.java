package com.aliens.backend.uploader;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UploadFileRequest(List<MultipartFile> files) {
}