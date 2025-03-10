package com.betsanddice.craps.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Schema(
        name = "Betsanddice-craps",
        description = "Schema to hold the statistics of CrapsGames for a given user"
)
@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonPropertyOrder({"id_user", "name_game", "games_played", "games_won", "percent_games_won", "amount_bet", "profit_obtained"})
public class UserCrapsGameStatsDto {

    @JsonProperty(value = "id_user")
    private UUID userId;

    @JsonProperty(value = "name_game")
    private String nameGame;

    @JsonProperty(value = "games_played")
    private int gamesPlayed;

    @JsonProperty(value = "games_won")
    private int gamesWon;

    @JsonProperty(value = "percent_games_won")
    private double percentGamesWon;

    @JsonProperty(value = "total_amount_bet")
    private double totalAmountBet;

    @JsonProperty(value = "profit_obtained")
    private double profitObtained;

}