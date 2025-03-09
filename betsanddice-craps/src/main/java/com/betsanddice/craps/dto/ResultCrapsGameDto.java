package com.betsanddice.craps.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonPropertyOrder({"attempts", "player_wins", "betting_odds", "amount_returned"})
public class ResultCrapsGameDto {

    @JsonProperty(value = "attempts")
    private Integer attempts;

    @JsonProperty(value = "player_wins")
    private boolean playerWins;

    @JsonProperty(value = "betting_odds")
    private double bettingOdds;

    @JsonProperty(value = "amount_returned")
    private double amountReturned;

}