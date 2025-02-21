package com.betsanddice.craps.exception;

import com.betsanddice.craps.dto.ErrorResponseDto;
import com.betsanddice.craps.dto.MessageDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<MessageDto> handleResponseStatusException(ResponseStatusException ex) {
//        HttpStatus statusCode = (HttpStatus) ex.getStatusCode();
//        String errorMessage;
//        Object[] detailMessageArguments = ex.getDetailMessageArguments();
//        if (detailMessageArguments == null || detailMessageArguments.length == 0) {
//            errorMessage = "Validation failed";
//        } else {
//            errorMessage = Arrays.stream(detailMessageArguments)
//                    .skip(1)
//                    .map(Object::toString)
//                    .collect(Collectors.joining(", "));
//            errorMessage = errorMessage.replace("[", "").replace("]", "");
//        }
//        MessageDto errorResponseMessage = new MessageDto(errorMessage);
//        return ResponseEntity.status(statusCode).body(errorResponseMessage);
//    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception,
                                                                  WebRequest webRequest) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(BadUuidException.class)
    public ResponseEntity<MessageDto> handleBadUuidException(BadUuidException ex) {
        return ResponseEntity.badRequest().body(new MessageDto(ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MessageDto> handleConstraintViolation(ConstraintViolationException ex) {
        String constraintMessage = ex.getConstraintViolations()
                .stream().findFirst().map(ConstraintViolation::getMessage).orElse("Invalid value");
        return ResponseEntity.badRequest().body(new MessageDto(constraintMessage));

    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<MessageDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//        return ResponseEntity.badRequest().body(new MessageDto(ex.getMessage()));
//    }

}



