package com.betsanddice.user.validator;

import com.betsanddice.user.annotation.UuidValidPattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UuidValidator implements ConstraintValidator<UuidValidPattern, String> {

    //private String message;
    //private Pattern pattern;

    private final String PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    @Override
    public void initialize(UuidValidPattern uuidValidPattern) {

/*        this.message = uuidValidPattern.message();
        this.pattern = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
                Pattern.CASE_INSENSITIVE);*/
    }

    @Override
    public boolean isValid(String userId, ConstraintValidatorContext context) {
        return userId.matches(this.PATTERN);

        /*
        boolean validUUID = !StringUtils.isEmpty(userId) && pattern.matcher(userId).matches();
        if (!validUUID) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
            return false;
        }
        return true;*/
    }

}
