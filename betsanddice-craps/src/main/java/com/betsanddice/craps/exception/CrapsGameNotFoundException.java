package com.betsanddice.craps.exception;

public class CrapsGameNotFoundException extends RuntimeException {

    public CrapsGameNotFoundException(String message) {
        super(message);
    }
}