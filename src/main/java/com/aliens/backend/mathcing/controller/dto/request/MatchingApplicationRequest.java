package com.aliens.backend.mathcing.controller.dto.request;

import com.aliens.backend.mathcing.service.model.Language;

public record MatchingApplicationRequest(
        Language firstPreferLanguage,
        Language secondPreferLanguage
) {
}
