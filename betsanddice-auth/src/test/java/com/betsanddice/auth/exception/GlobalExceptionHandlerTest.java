package com.betsanddice.auth.exception;

import com.betsanddice.auth.dto.MessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

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
}