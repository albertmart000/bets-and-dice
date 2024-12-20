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

    @Field(name = "amount_bet")
    private BigDecimal amountBet;

    @Field(name = "player_result")
    private int playerResult;
}

