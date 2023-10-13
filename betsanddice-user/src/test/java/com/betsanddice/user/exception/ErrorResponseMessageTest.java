package com.betsanddice.user.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ErrorResponseMessage.class)
class ErrorResponseMessageTest {

    @MockBean
    private ErrorResponseMessage errorResponseMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testErrorResponseMessage_WhenReturn_StatusCodeAndErrorMessage() {
        int STATUS_CODE = 500;
        String ERROR_MESSAGE = "INTERNAL SERVER ERROR";
        HttpStatus INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponseMessage response = new ErrorResponseMessage(STATUS_CODE, ERROR_MESSAGE);

        StepVerifier.create(Mono.just(response))
                .expectNextMatches(resp -> {
                    assertEquals(INTERNAL_SERVER_ERROR.value(), response.getStatusCode());
                    assertEquals("INTERNAL SERVER ERROR", response.getMessage());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testErrorResponseMessage_WhenReturn_ErrorsAndErrorMessage() {
        Map<String, String> errors = Map.of("Field1", "Message1", "Field2", "Message2");
        String message = "Parameter not valid";
        ErrorResponseMessage response = new ErrorResponseMessage(message, errors);

        StepVerifier.create(Mono.just(response))
                .expectNextMatches(resp -> {
                    assertEquals("Parameter not valid", response.getMessage());
                    assertEquals(errors, response.getErrors());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testErrorResponseMessage_WhenOnlyReturn_ErrorMessage() {
        String message = "Message";
        ErrorResponseMessage response = new ErrorResponseMessage(message);

        StepVerifier.create(Mono.just(response))
                .expectNextMatches(resp -> {
                    assertEquals("Message", response.getMessage());
                    return true;
                })
                .verifyComplete();
    }

}