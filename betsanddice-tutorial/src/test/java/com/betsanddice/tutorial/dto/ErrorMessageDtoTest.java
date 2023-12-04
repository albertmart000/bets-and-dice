package com.betsanddice.tutorial.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ErrorMessageDto.class)
class ErrorMessageDtoTest {
    private ErrorMessageDto errorMessageDto;
    private ErrorMessageDto errorMessageDto1;

    private ErrorMessageDto errorMessageDtoWithStatusCode;
    private ErrorMessageDto errorMessageDtoWithStatusCode1;

    private ErrorMessageDto errorMessageDtoWithMapErrors;
    private ErrorMessageDto errorMessageDtoWithMapErrors1;

    private Map<String, String> errors = new HashMap<>();

    private final String EXPECTED_MESSAGE = "Expected message";
    private final String ANOTHER_DIFFERENT_MESSAGE = "Another different message";
    private final String PARAMETER_NOT_VALID = "Parameter not valid";

    private final String INTERNAL_SERVER_ERROR = "INTERNAL SERVER ERROR";
    private final int STATUS_CODE = 500;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        errorMessageDto = new ErrorMessageDto(EXPECTED_MESSAGE);

        errorMessageDtoWithStatusCode = new ErrorMessageDto(INTERNAL_SERVER_ERROR, STATUS_CODE);

        errors = Map.of("Field1", "DefaultMessage1", "Field2", "DefaultMessage2");
        errorMessageDtoWithMapErrors = new ErrorMessageDto(PARAMETER_NOT_VALID, errors);

    }
    @Test
    void errorMessageTest() {
        Assertions.assertEquals(EXPECTED_MESSAGE, errorMessageDto.getMessage());
    }

    @Test
    void notExpectedErrorMessageTest() {
        Assertions.assertNotEquals(ANOTHER_DIFFERENT_MESSAGE, errorMessageDto.getMessage());
    }

    @Test
    void givenTwoErrorsMessage_Equals_hashCodesShouldBeSame_Test() {
        errorMessageDto1 = new ErrorMessageDto(EXPECTED_MESSAGE);

        Assertions.assertEquals(errorMessageDto.hashCode(), errorMessageDto1.hashCode());
        Assertions.assertTrue(errorMessageDto.equals(errorMessageDto1)
                && errorMessageDto1.equals(errorMessageDto));
    }

    @Test
    void givenTwoErrorsMessage_NonEquals_hashCodesShouldBeDifferent_Test() {
        errorMessageDto1 = new ErrorMessageDto(ANOTHER_DIFFERENT_MESSAGE);

        Assertions.assertNotEquals(errorMessageDto.hashCode(), errorMessageDto1.hashCode());
        Assertions.assertFalse(errorMessageDto.equals(errorMessageDto1)
                && errorMessageDto1.equals(errorMessageDto));
    }

    @Test
    void errorMessageWithStatusCodeTest() {
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;

        StepVerifier.create(Mono.just(errorMessageDtoWithStatusCode))
                .expectNextMatches(resp -> {
                    assertEquals(internalServerError.value(), errorMessageDtoWithStatusCode.getStatusCode());
                    assertEquals("INTERNAL SERVER ERROR", errorMessageDtoWithStatusCode.getMessage());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void givenTwoErrorMessageWithStatusCode_Equals_hashCodesShouldBeSame_Test() {
        errorMessageDtoWithStatusCode1 = new ErrorMessageDto(INTERNAL_SERVER_ERROR, STATUS_CODE);

        Assertions.assertEquals(errorMessageDtoWithStatusCode.hashCode(), errorMessageDtoWithStatusCode1.hashCode());
        Assertions.assertTrue(errorMessageDtoWithStatusCode.equals(errorMessageDtoWithStatusCode1)
                && errorMessageDtoWithStatusCode1.equals(errorMessageDtoWithStatusCode));
    }

    @Test
    void givenTwoErrorMessageWithStatusCode_NonEquals_hashCodesShouldBeDifferent_Test() {
        String message1 = "SERVICE UNAVAILABLE";
        int statusCode1 = 503;
        errorMessageDtoWithStatusCode1 = new ErrorMessageDto(message1, statusCode1);

        Assertions.assertNotEquals(errorMessageDtoWithStatusCode.hashCode(), errorMessageDtoWithStatusCode1.hashCode());
        Assertions.assertFalse(errorMessageDtoWithStatusCode.equals(errorMessageDtoWithStatusCode1)
                && errorMessageDtoWithStatusCode1.equals(errorMessageDtoWithStatusCode));
    }

    @Test
    void errorMessageWithMapErrorsTest() {
        StepVerifier.create(Mono.just(errorMessageDtoWithMapErrors))
                .expectNextMatches(resp -> {
                    assertEquals("Parameter not valid", errorMessageDtoWithMapErrors.getMessage());
                    assertEquals(errors, errorMessageDtoWithMapErrors.getErrors());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void givenTwoErrorMessageWithMapErrors_Equals_hashCodesShouldBeSame_Test() {
        errorMessageDtoWithMapErrors1 = new ErrorMessageDto(PARAMETER_NOT_VALID, errors);

        Assertions.assertEquals(errorMessageDtoWithMapErrors.hashCode(), errorMessageDtoWithMapErrors1.hashCode());
        Assertions.assertTrue(errorMessageDtoWithMapErrors.equals(errorMessageDtoWithMapErrors1)
                && errorMessageDtoWithMapErrors1.equals(errorMessageDtoWithMapErrors));
    }

    @Test
    void givenTwoErrorMessageWithMapErrors_NonEquals_hashCodesShouldBeDifferent_Test() {
        Map<String, String> errors1 = Map.of("Field3", "DefaultMessage3", "Field4", "DefaultMessage4");
        errorMessageDtoWithMapErrors1 = new ErrorMessageDto(PARAMETER_NOT_VALID, errors1);

        Assertions.assertNotEquals(errorMessageDtoWithMapErrors.hashCode(), errorMessageDtoWithMapErrors1.hashCode());
        Assertions.assertFalse(errorMessageDtoWithMapErrors.equals(errorMessageDtoWithMapErrors1)
                && errorMessageDtoWithMapErrors1.equals(errorMessageDtoWithMapErrors));
    }

}