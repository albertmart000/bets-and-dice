package com.betsanddice.user.annotations;

import com.betsanddice.user.validator.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    String message() default "Invalid Email format: email should be in a valid format.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

