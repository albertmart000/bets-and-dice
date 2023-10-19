package com.betsanddice.user.annotation;

import com.betsanddice.user.validator.UuidValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = UuidValidator.class)
@Documented
public @interface UuidValidPattern {
    String message() default "Invalid ID format. Please indicate the correct format.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
