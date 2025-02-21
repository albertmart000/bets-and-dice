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
//    @Min(value = 2, message = "The expected dice sum must be greater than 2 and less than 12")
//    @Max(value = 12, message = "The expected dice sum must be greater than 2 and less than 12")
    private int expectedDiceSum;

    @JsonProperty(value = "expected_attempts")
//    @Pattern(regexp = "^(1,3,5,7)$", message = "The expected attempts must be 1, 3, 5 or 7")
    private int expectedAttempts;

    @JsonProperty(value = "amount_bet")
//    @Min(value = 1, message = "The bet amount must be greater than 1 and less than 100")
//    @Max(value = 100, message = "The bet amount must be greater than 1 and less than 100")
    private double amountBet;
}
