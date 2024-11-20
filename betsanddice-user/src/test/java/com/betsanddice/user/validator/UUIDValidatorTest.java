package com.betsanddice.user.validator;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UUIDValidatorTest {

    private final UUIDValidator validator = new UUIDValidator();

    @Test
    void isValid_withNullUUID_returnsFalse() {
        assertFalse(validator.isValid(null, null));
    }

    @Test
    void isValid_withValidUUID_returnsTrue() {
        assertTrue(validator.isValid(UUID.randomUUID(), null));
    }

}