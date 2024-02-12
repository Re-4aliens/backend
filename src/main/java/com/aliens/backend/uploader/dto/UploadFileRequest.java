package com.aliens.backend.uploader.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UploadFileRequest(List<MultipartFile> files) {
}