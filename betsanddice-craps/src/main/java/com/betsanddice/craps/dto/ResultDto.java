package com.betsanddice.craps.dto;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultDto {

    @Field(name = "attempts")
    private Integer attempts;

    @Field(name = "player_wins")
    private boolean playerWins;

    @Field(name = "betting_odds")
    private double bettingOdds;

    @Field(name = "amount_returned")
    private double amountReturned;

}