package com.betsanddice.craps.document;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BetDocument {

    @Field(name = "expected_dice_sum")
    private int expectedDiceSum;

    @Field(name = "expected_attempts")
    private int expectedAttempts;

    @Field(name = "amount_bet")
    private BigDecimal amountBet;

}