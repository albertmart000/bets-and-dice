package com.betsanddice.user.validator;

import com.betsanddice.user.annotation.UuidValidPattern;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class UuidValidator implements ConstraintValidator <UuidValidPattern, String> {

    private String message;
    private Pattern pattern;

    @Override
    public void initialize(UuidValidPattern uuidValidPattern) {
        this.message = uuidValidPattern.message();
        this.pattern = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
                Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean isValid(String userId, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(userId) || pattern.matcher(userId).matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }
        return !StringUtils.isEmpty(userId) && pattern.matcher(userId).matches();
    }

}
