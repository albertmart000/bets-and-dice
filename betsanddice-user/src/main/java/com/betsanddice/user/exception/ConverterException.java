package com.betsanddice.user.exception;

public class ConverterException extends RuntimeException {
    public ConverterException(String message, Exception e) {
        super(message, e);
    }
}