package com.betsanddice.tutorial.dto.in;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GameTutorialDtoByName {

    @JsonProperty(value = "game_name")
    @NotBlank(message = "Invalid name: the name of the game can't be empty.")
    @NotBlank(message = "Invalid name: the name of the game can't be empty.")
    @Size(min = 2, message = "Invalid Name: the name of the game should have at least 2 characters.")
    private String gameName;

    @JsonProperty(value = "rules")
    @NotBlank(message = "Invalid text: the text of the rules of the game can't be empty.")
    @NotBlank(message = "Invalid text: the text of the rules of the game can't be empty.")
    private String rules;

}
