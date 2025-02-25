package com.betsanddice.craps.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"attempts", "player_wins", "betting_odds", "amount_returned"})
public class ResultDto {

    @JsonProperty(value = "attempts")
    private Integer attempts;

    @JsonProperty(value = "player_wins")
    private boolean playerWins;

    @JsonProperty(value = "betting_odds")
    private double bettingOdds;

    @JsonProperty(value = "amount_returned")
    private double amountReturned;

}