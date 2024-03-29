package com.betsanddice.tutorial.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class GameTutorialDto {
    @JsonProperty(value = "game_tutorial_id", index = 0)
    private UUID gameTutorialId;

    @JsonProperty(value = "game_id", index = 1)
    private UUID gameId;

    @JsonProperty(value = "game_name", index = 2)
    private String gameName;

    @JsonProperty(value = "rules", index = 3)
    private String rules;

    public GameTutorialDto(String gameName, String rules) {
        this.gameName = gameName;
        this.rules = rules;
    }
}
