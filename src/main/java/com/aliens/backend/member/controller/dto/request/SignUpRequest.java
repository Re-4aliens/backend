package com.aliens.backend.member.controller.dto.request;

public record SignUpRequest(String email,
                            String password,
                            String name,
                            String mbti,
                            String gender,
                            String nationality,
                            String birthday,
                            String aboutMe) {
}