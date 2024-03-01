package com.betsanddice.game.exception;

import com.betsanddice.game.dto.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorMessageDto> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatus statusCode = (HttpStatus) ex.getStatusCode();
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(ex.getMessage(), statusCode.value());
        return ResponseEntity.status(statusCode).body(errorMessageDto);
    }

    @ExceptionHandler(BadUuidException.class)
    public ResponseEntity<ErrorMessageDto> handleBadUUID(BadUuidException ex) {
        return ResponseEntity.badRequest().body(new ErrorMessageDto(ex.getMessage()));
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ErrorMessageDto> handleGameNotFoundException(GameNotFoundException ex) {
        return ResponseEntity.badRequest().body(new ErrorMessageDto(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ErrorMessageDto("Parameter not valid", errors));
    }

}




