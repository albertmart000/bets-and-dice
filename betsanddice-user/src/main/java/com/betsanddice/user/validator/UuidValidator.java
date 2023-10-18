package com.betsanddice.user.validator;

import com.betsanddice.user.annotation.UuidPattern;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class UuidValidator implements ConstraintValidator <UuidPattern, String> {
    @Override
    public void initialize(UuidPattern constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String uuid, ConstraintValidatorContext context) {
        Pattern uuidValidPattern = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
                Pattern.CASE_INSENSITIVE);
        return !StringUtils.isEmpty(uuid) && uuidValidPattern.matcher(uuid).matches();
    }

}
