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
@JsonPropertyOrder ({"expectedDiceSum", "expectedAttempts", "amountBet"})
public class BetDto {

    @JsonProperty(value = "expected_dice_sum")
    private int expectedDiceSum;

    @JsonProperty(value = "expected_attempts")
    private int expectedAttempts;

    @JsonProperty(value = "amount_bet")
    private double amountBet;

}