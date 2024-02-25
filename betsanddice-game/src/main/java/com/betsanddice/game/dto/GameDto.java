package com.betsanddice.game.dto;

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
public class GameDto {

    @JsonProperty(value = "game_id", index = 0)
    private UUID uuid;

    @JsonProperty(value = "game_name", index = 1)
    private String gameName;

    @JsonProperty(value = "tutorial_id", index = 2)
    private UUID tutorialId;

    @JsonProperty(value = "stat_id", index = 3)
    private UUID statId;
}
