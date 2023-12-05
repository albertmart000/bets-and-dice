package com.betsanddice.user.exception;

import com.betsanddice.user.dto.ErrorMessageDto;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = GlobalExceptionHandlerTest.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;
    @MockBean
    private ResponseStatusException responseStatusException;
    @MockBean
    private MethodArgumentNotValidException methodArgumentNotValidException;
    @MockBean
    private ErrorMessageDto errorMessageDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleResponseStatusException_Test() {
        String REQUEST = "Invalid request";
        HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;

        when(responseStatusException.getStatusCode()).thenReturn(BAD_REQUEST);
        when(responseStatusException.getMessage()).thenReturn(REQUEST);
        when(errorMessageDto.getStatusCode()).thenReturn(BAD_REQUEST.value());
        when(errorMessageDto.getMessage()).thenReturn(REQUEST);

        ErrorMessageDto expectedErrorMessage = new ErrorMessageDto(REQUEST, BAD_REQUEST.value());
        expectedErrorMessage.setStatusCode(BAD_REQUEST.value());
        expectedErrorMessage.setMessage(REQUEST);

        ResponseEntity<ErrorMessageDto> response = globalExceptionHandler.handleResponseStatusException(responseStatusException);

        StepVerifier.create(Mono.just(response))
                .expectNextMatches(resp -> {
                    assertEquals(BAD_REQUEST, response.getStatusCode());
                    assertEquals(expectedErrorMessage, response.getBody());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void handleMethodArgumentNotValidException_Test() {

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("object", "field", "errorMessage")));
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ErrorMessageDto> responseEntity = globalExceptionHandler.handleMethodArgumentNotValidException(methodArgumentNotValidException);

        MatcherAssert.assertThat(responseEntity, notNullValue());
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

        ResponseEntity<ErrorMessageDto> responseEntity = globalExceptionHandler.handleMethodArgumentNotValidException(methodArgumentNotValidException);

        MatcherAssert.assertThat(responseEntity, notNullValue());
    }

}





