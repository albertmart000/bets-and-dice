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
public class UserStatByGameDto {

    @JsonProperty(value = "user_stat_by_game_id", index = 0)
    private UUID uuid;

    @JsonProperty(value = "user_id", index = 1)
    private UUID userId;

    @JsonProperty(value = "game_id", index = 2)
    private UUID gameId;

    @JsonProperty(value = "user_nickname", index = 3)
    private String userNickname;

    @JsonProperty(value = "game_name", index = 4)
    private String gameName;

    @JsonProperty(value = "games_played", index = 5)
    private int gamesPlayed;

    @JsonProperty(value = "number_games_won", index = 6)
    private int numberGamesWon;

    @JsonProperty(value = "percentage_games_won", index = 7)
    private double percentageGamesWon;

    @JsonProperty(value = "ranking_by_number", index = 8)
    private int rankingByNumber;

    @JsonProperty(value = "ranking_by_percentage", index = 9)
    private int rankingByPercentage;

}
