package com.betsanddice.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonPropertyOrder({"user_id", "email"})
public class User {

    @JsonProperty(value = "user_id")
    private UUID uuid;

    @JsonProperty(value = "email")
    private String email;

}
