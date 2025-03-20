package com.betsanddice.user.exception;

public class CrapsGameNotFoundException extends RuntimeException {

    public CrapsGameNotFoundException(String message) {
        super(message);
    }
}