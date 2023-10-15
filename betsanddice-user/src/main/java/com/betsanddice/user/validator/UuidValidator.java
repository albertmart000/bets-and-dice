package com.betsanddice.user.validator;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UuidValidator {

    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Invalid ID format. Please indicate the correct format.")
    private String usedId;

}
