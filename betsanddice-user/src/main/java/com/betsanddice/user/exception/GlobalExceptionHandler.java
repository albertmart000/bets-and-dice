package com.betsanddice.user.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseMessage> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatus statusCode = (HttpStatus) ex.getStatusCode();
        ErrorResponseMessage errorResponseMessage = new ErrorResponseMessage(statusCode.value(), ex.getReason());
        return ResponseEntity.status(statusCode).body(errorResponseMessage);
    }

    @ExceptionHandler(BadUuidException.class)
    public ResponseEntity<ErrorResponseMessage> handleBadUUID(BadUuidException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponseMessage(ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseMessage> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponseMessage(ex.getMessage()));
    }

    @ExceptionHandler(ConverterException.class)
    public ResponseEntity<ErrorResponseMessage> handleConverterException(UserNotFoundException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponseMessage(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ErrorResponseMessage("Parameter not valid", errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseMessage> handleConstraintViolationException(ConstraintViolationException ex) {
       // List<String> errors = new ArrayList<>();
        ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(new ErrorResponseMessage(ex.getMessage()));



/*        List<String> constraints = new ArrayList<>();
        ex.getConstraintViolations().forEach(constraint->
                constraints.add(constraint.getMessageTemplate()));
        return ResponseEntity.badRequest().body(new ErrorResponseMessage(ex.getMessage()));*/
    }
}
/*

       @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseMessageDto> handleConstraintViolation(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest().body(new ErrorResponseMessageDto( 400, errorMessage));
    }
*/

