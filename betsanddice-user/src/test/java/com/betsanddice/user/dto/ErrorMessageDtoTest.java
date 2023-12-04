package com.betsanddice.user.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ErrorMessageDto.class)
class ErrorMessageDtoTest {

    @Test
    void messageTest() {
        String message = "Expected message";
        ErrorMessageDto errorMessageResponse = new ErrorMessageDto(message);
        Assertions.assertEquals(message, errorMessageResponse.getMessage());
    }

    @Test
    void notExpectedMessageTest() {
        String message = "Expected message";
        String notExpectedMessage = "Not expected message.";
        ErrorMessageDto errorMessageResponse = new ErrorMessageDto(message);
        Assertions.assertNotEquals(notExpectedMessage, errorMessageResponse.getMessage());
    }

    @Test
    void errorResponseMessageTest() {
        String message = "INTERNAL SERVER ERROR";
        int statusCode = 500;
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorMessageDto errorMessageResponse = new ErrorMessageDto(message, statusCode);

        StepVerifier.create(Mono.just(errorMessageResponse))
                .expectNextMatches(resp -> {
                    assertEquals(internalServerError.value(), errorMessageResponse.getStatusCode());
                    assertEquals("INTERNAL SERVER ERROR", errorMessageResponse.getMessage());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void errorMessageTest() {
        Map<String, String> errors = Map.of("Field1", "DefaultMessage1", "Field2", "DefaultMessage2");
        String message = "Parameter not valid";
        ErrorMessageDto errorMessageResponse = new ErrorMessageDto(message, errors);

        StepVerifier.create(Mono.just(errorMessageResponse))
                .expectNextMatches(resp -> {
                    assertEquals("Parameter not valid", errorMessageResponse.getMessage());
                    assertEquals(errors, errorMessageResponse.getErrors());
                    return true;
                })
                .verifyComplete();
    }

}