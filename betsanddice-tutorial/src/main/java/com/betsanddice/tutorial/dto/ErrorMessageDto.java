package com.betsanddice.tutorial.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ErrorMessageDto {

    private String message;
    private int statusCode;
    private Map<String, String> errors = new HashMap<>();

    public ErrorMessageDto(String message) {
        this.message = message;
    }

    public ErrorMessageDto(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public ErrorMessageDto(String message, Map<String, String> errors) {
        this.message = message;
        this.errors = errors;
    }

}
