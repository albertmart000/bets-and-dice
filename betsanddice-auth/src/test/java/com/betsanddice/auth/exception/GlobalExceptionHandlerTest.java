package com.betsanddice.auth.exception;

import com.betsanddice.auth.dto.MessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final HttpStatus OK_REQUEST = HttpStatus.OK;

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleCustomBadRequestException() {
        String errorMessage = "Invalid request data";
        CustomBadRequestException exception = new CustomBadRequestException(errorMessage);

        ResponseEntity<MessageDto> response = globalExceptionHandler.handleCustomBadRequestException(exception);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().getMessage());
    }

    @Test
    void testHandleUserNotFoundException() {
        UserNotFoundException userNotFoundException = new UserNotFoundException("User not found");

        ResponseEntity<MessageDto> responseEntity = globalExceptionHandler.handleUserNotFoundException(userNotFoundException);

        assertEquals(OK_REQUEST, responseEntity.getStatusCode());
        String responseBody = Objects.requireNonNull(responseEntity.getBody()).getMessage();
        assertTrue(responseBody.contains("User not found"));
    }

}