package com.betsanddice.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
public class MessageDto {

    @JsonProperty("message")
    private String message;

    public MessageDto(String message) {
        this.message = message;
    }
}
