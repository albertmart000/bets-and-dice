package com.betsanddice.game.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
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
