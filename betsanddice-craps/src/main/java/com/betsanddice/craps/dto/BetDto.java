package com.betsanddice.craps.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BetDto {

    @JsonProperty(value = "expected_dice_sum")
    private int expectedDiceSum;

    @JsonProperty(value = "expected_attempts")
    private int expectedAttempts;

    @JsonProperty(value = "amount_bet")
    private double amountBet;

}