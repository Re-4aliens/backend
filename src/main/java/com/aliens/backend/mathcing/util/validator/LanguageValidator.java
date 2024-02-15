package com.aliens.backend.mathcing.util.validator;


import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.aliens.backend.mathcing.business.model.Language;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class LanguageValidator implements ConstraintValidator<LanguageCheck, MatchingApplicationRequest> {
    @Override
    public boolean isValid(final MatchingApplicationRequest value, final ConstraintValidatorContext context) {
        Language firstPreferLanguage = value.firstPreferLanguage();
        Language secondPreferLanguage = value.secondPreferLanguage();

        if (firstPreferLanguage.equals(secondPreferLanguage)) {
            throw new RestApiException(MatchingError.INVALID_LANGUAGE_INPUT);
        }
        return true;
    }

    @Override
    public void initialize(final LanguageCheck constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}