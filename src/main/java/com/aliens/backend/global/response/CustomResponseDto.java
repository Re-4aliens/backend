package com.aliens.backend.global.response;

public record CustomResponseDto<T>(String code, T result) {
}
