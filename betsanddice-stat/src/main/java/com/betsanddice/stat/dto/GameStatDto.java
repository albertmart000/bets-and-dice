package com.betsanddice.stat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class GameStatDto {

    @JsonProperty(value = "game_stat_id", index = 0)
    private UUID uuid;

    @JsonProperty(value = "game_id", index = 1)
    private UUID gameId;

    @JsonProperty(value = "game_name", index = 2)
    private String gameName;

    @JsonProperty(value = "number_players", index = 3)
    private int numberPlayers;

    @JsonProperty(value = "number_games_played", index = 4)
    private int numberGamesPlayed;

    @JsonProperty(value = "ranking_by_number", index = 5)
    private int rankingByNumber;

    @JsonProperty(value = "ranking_by_percentage", index = 6)
    private int rankingByPercentage;
}
