package com.betsanddice.tutorial.exception;

import com.betsanddice.tutorial.dto.ErrorMessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadUuidException.class)
    public ResponseEntity<ErrorMessageDto> handleBadUUID(BadUuidException ex) {
        return ResponseEntity.badRequest().body(new ErrorMessageDto(ex.getMessage()));
    }

    @ExceptionHandler(GameTutorialNotFoundException.class)
    public ResponseEntity<ErrorMessageDto> handleGameTutorialNotFoundException(GameTutorialNotFoundException ex) {
        return ResponseEntity.badRequest().body(new ErrorMessageDto(ex.getMessage()));
    }

    @ExceptionHandler(GameTutorialAlreadyExistException.class)
    public ResponseEntity<ErrorMessageDto> handleGameTutorialAlreadyExistException(GameTutorialAlreadyExistException ex) {
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




