package com.betsanddice.auth.exception;

import com.betsanddice.auth.dto.MessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<MessageDto> handleCustomBadRequestException(CustomBadRequestException ex) {
        return ResponseEntity.ok().body(new MessageDto(ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<MessageDto> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.ok().body(new MessageDto(ex.getMessage()));
    }
}