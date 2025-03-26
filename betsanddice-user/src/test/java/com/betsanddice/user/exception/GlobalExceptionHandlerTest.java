package com.betsanddice.user.exception;

import com.betsanddice.user.dto.MessageDto;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = GlobalExceptionHandlerTest.class)
class GlobalExceptionHandlerTest {

    private final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
    private final HttpStatus OK_REQUEST = HttpStatus.OK;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private ResponseStatusException responseStatusException;
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        responseStatusException = mock(ResponseStatusException.class);
        methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
    }

    @Test
    void testHandleResponseStatusException() {
        String expectedErrorMessage = "Validation failed";
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        ResponseStatusException ex = new ResponseStatusException(expectedStatus, expectedErrorMessage);

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<MessageDto> responseEntity = handler.handleResponseStatusException(ex);

        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertEquals(expectedErrorMessage, responseEntity.getBody().getMessage());
    }

    @Test
    void testHandleResponseStatusException_NullDetailMessageArguments() {
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        ResponseStatusException ex = mock(ResponseStatusException.class);
        when(ex.getStatusCode()).thenReturn(expectedStatus);
        when(ex.getDetailMessageArguments()).thenReturn(null);

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<MessageDto> responseEntity = handler.handleResponseStatusException(ex);

        assertEquals(expectedStatus, responseEntity.getStatusCode());
        assertEquals("Validation failed", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testHandleBadUuidException() {
        BadUuidException badUUIDException = new BadUuidException("Invalid Id format");

        ResponseEntity<MessageDto> responseEntity = globalExceptionHandler.handleBadUuidException(badUUIDException);

        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        String responseBody = Objects.requireNonNull(responseEntity.getBody()).getMessage();
        assertTrue(responseBody.contains("Invalid Id format"));
    }

    @Test
    void testHandleUserNotFoundException() {
        UserNotFoundException userNotFoundException = new UserNotFoundException("User not found");

        ResponseEntity<MessageDto> responseEntity = globalExceptionHandler.handleUserNotFoundException(userNotFoundException);

        assertEquals(OK_REQUEST, responseEntity.getStatusCode());
        String responseBody = Objects.requireNonNull(responseEntity.getBody()).getMessage();
        assertTrue(responseBody.contains("User not found"));
    }


    @Test
    void handleMethodArgumentNotValidException_Test() {
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("object", "field", "errorMessage")));
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<MessageDto> responseEntity = globalExceptionHandler.handleMethodArgumentNotValidException(methodArgumentNotValidException);

        MatcherAssert.assertThat(responseEntity, notNullValue());
    }

    @Test
    void TestHandleCrapsGameNotFoundException() {
        CrapsGameNotFoundException crapsGameNotFoundException = new CrapsGameNotFoundException("CrapsGame not found");

        ResponseEntity<MessageDto> responseEntity = globalExceptionHandler.handleCrapsGameNotFoundException(crapsGameNotFoundException);

        assertEquals(OK_REQUEST, responseEntity.getStatusCode());
        String responseBody = Objects.requireNonNull(responseEntity.getBody()).getMessage();
        Assertions.assertTrue(responseBody.contains("CrapsGame not found"));
    }

    @Test
    void handleMethodArgumentNotValidException_Return_ErrorMessage_Test() {
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        FieldError fieldError = Mockito.mock(FieldError.class);
        when(fieldError.getField()).thenReturn("name");
        when(fieldError.getDefaultMessage()).thenReturn("errorMessage");
        when(fieldError.getCodes()).thenReturn(new String[]{"message"});
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<MessageDto> responseEntity = globalExceptionHandler.handleMethodArgumentNotValidException(methodArgumentNotValidException);

        MatcherAssert.assertThat(responseEntity, notNullValue());
    }
}





