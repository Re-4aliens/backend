package com.aliens.backend.member.controller.dto;

public record EncodedMemberPage(String mbti,
                                String gender,
                                String nationality,
                                String birthday,
                                String aboutMe) {
}
