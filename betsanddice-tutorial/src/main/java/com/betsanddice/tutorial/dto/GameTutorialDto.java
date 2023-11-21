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
    @JsonProperty(value = "game_id", index = 0)
    private UUID gameId;

    @JsonProperty(value = "game_name", index = 1)
    private String gameName;

    @JsonProperty(value = "rules", index = 2)
    private String rules;

}
