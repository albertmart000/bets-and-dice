package com.betsanddice.stat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GameStatDto {

    @JsonProperty(value = "game_stat_id", index = 0)
    private UUID uuid;

    @JsonProperty(value = "game_id", index = 1)
    private UUID gameId;

    @JsonProperty(value = "game_name", index = 2)
    private String gameName;

    @JsonProperty(value = "games_played", index = 3)
    private int gamesPlayed;

    @JsonProperty(value = "users_ranking", index = 4)
    private List<UserGameStatDto> usersRanking;
}
