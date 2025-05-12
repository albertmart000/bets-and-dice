package com.betsanddice.user.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EmailValidatorTest {

    private EmailValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new EmailValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void shouldReturnTrueForValidEmails() {
        assertTrue(validator.isValid("user@example.com", context));
        assertTrue(validator.isValid("user.name+alias@domain.co", context));
        assertTrue(validator.isValid("user_name@sub.domain.org", context));
    }

    @Test
    void shouldReturnTrueForEmailsWithSubdomains() {
        assertTrue(validator.isValid("user@mail.sub.domain.com", context));
        assertTrue(validator.isValid("user@sub.domain.co.uk", context));
    }

    @Test
    void shouldReturnTrueForEmailsWithSpecialCharacters() {
        assertTrue(validator.isValid("user+alias@example.com", context));
        assertTrue(validator.isValid("user%name@domain.org", context));
        assertTrue(validator.isValid("user_name@domain.co", context));
    }

    @Test
    void shouldReturnFalseForInvalidEmails() {
        assertFalse(validator.isValid(null, context));
        assertFalse(validator.isValid("", context));
        assertFalse(validator.isValid("invalid-email", context));
    }

    @Test
    void shouldReturnFalseForEmailsWithInvalidCharacters() {
        assertFalse(validator.isValid("user@domain,com", context));
        assertFalse(validator.isValid("user@domain#com", context));
    }

    @Test
    void shouldReturnFalseForEmailsWithSpaces() {
        assertFalse(validator.isValid(" user@example.com", context));
        assertFalse(validator.isValid("user@example.com ", context));
        assertFalse(validator.isValid("user @example.com", context));
    }

    @Test
    void shouldReturnFalseForEmailsWithMissingParts() {
        assertFalse(validator.isValid("@example.com", context));
        assertFalse(validator.isValid("user@", context));
        assertFalse(validator.isValid("user@.com", context));
        assertFalse(validator.isValid("user@domain", context));
    }
}