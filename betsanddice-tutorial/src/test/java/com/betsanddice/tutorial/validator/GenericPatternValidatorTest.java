package com.betsanddice.tutorial.validator;

import com.betsanddice.tutorial.annotations.ValidGenericPattern;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenericPatternValidatorTest {

    @InjectMocks
    private GenericPatternValidator validator;
    @Mock
    private ValidGenericPattern constraintAnnotation;
    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(constraintAnnotation.pattern()).thenReturn("^\\d{1,9}$");

        validator.initialize(constraintAnnotation);
    }

    @Test
    void isValid() {
        boolean isValid = validator.isValid("13", context);
        assertTrue(isValid);
    }

    @Test
    void isNotValid() {
        boolean isValid = validator.isValid("1a23", context);
        assertFalse(isValid);
    }

    @Test
    void isTooLongNotValid() {
        boolean isValid = validator.isValid("1234567890", context);
        assertFalse(isValid);
    }
}