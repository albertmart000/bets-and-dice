package com.betsanddice.stat.dto;

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
public class UserGameStatDto {

    @JsonProperty(value = "user_game_stat_id", index = 0)
    private UUID uuid;

    @JsonProperty(value = "user_id", index = 1)
    private UUID userId;

    @JsonProperty(value = "game_id", index = 2)
    private UUID gameId;

    @JsonProperty(value = "game_name", index = 3)
    private String gameName;

    @JsonProperty(value = "games_played", index = 4)
    private int gamesPlayed;

    @JsonProperty(value = "games_won/attempts", index = 5)
    private int gamesWonOrAttempts;

    @JsonProperty(value = "average", index = 6)
    private double average;

    @JsonProperty(value = "ranking", index = 7)
    private int ranking;

}
