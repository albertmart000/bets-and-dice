package com.betsanddice.user.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
public class ErrorResponseMessage {
    private int statusCode;
    private String message;
    private Map<String, String> errors = new HashMap<>();

    public ErrorResponseMessage(String message) {
        this.message = message;
    }

    public ErrorResponseMessage(String message, Map<String, String> errors) {
        this.message = message;
        this.errors = errors;
    }

    public ErrorResponseMessage(int statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }

}