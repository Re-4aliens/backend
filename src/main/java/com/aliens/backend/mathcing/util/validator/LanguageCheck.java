package com.aliens.backend.mathcing.util.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({ PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {LanguageValidator.class})
@Documented
public @interface LanguageCheck {
    String message() default "Invalid Language Input";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}