package com.aliens.backend.member.controller.dto.response;


public record MemberPageResponse(String name,
                                String mbti,
                                String gender,
                                String nationality,
                                String birthday,
                                String selfIntroduction,
                                String profileImageURL)  {
}
