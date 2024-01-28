package com.aliens.backend.auth.controller.dto;

import com.aliens.backend.auth.domain.MemberRole;

public record LoginMember(Long memberId, MemberRole role) {
}
