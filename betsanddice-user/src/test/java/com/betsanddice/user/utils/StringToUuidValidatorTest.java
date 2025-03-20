package com.betsanddice.user.utils;

import com.betsanddice.user.exception.BadUuidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StringToUuidValidatorTest {

    private StringToUuidValidator validator;

    @BeforeEach
    void setUp() {
        validator = new StringToUuidValidator();
    }

    @Test
    void validateUuid_ShouldReturnUuid_WhenValidUuidIsProvided() {
        String validUuid = "706507d4-b89f-41eb-a7eb-41838d08a08f";

        StepVerifier.create(validator.validateUuid(validUuid))
                .assertNext(uuid -> {
                    assertNotNull(uuid);
                    assertEquals(UUID.fromString(validUuid), uuid);
                })
                .verifyComplete();
    }

    @Test
    void validateUuid_ShouldReturnError_WhenUuidIsInvalid() {
        String invalidUuid = "invalid-uuid";

        StepVerifier.create(validator.validateUuid(invalidUuid))
                .expectError(BadUuidException.class)
                .verify();
    }

    @Test
    void validateUuid_ShouldReturnError_WhenUuidIsNull() {
        String nullUuid = null;

        StepVerifier.create(validator.validateUuid(nullUuid))
                .expectError(BadUuidException.class)
                .verify();
    }

    @Test
    void validateUuid_ShouldReturnError_WhenUuidIsEmpty() {
        String emptyUuid = "";

        StepVerifier.create(validator.validateUuid(emptyUuid))
                .expectError(BadUuidException.class)
                .verify();
    }
}
