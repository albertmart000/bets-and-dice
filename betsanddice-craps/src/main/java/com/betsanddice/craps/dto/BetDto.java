package com.betsanddice.craps.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Schema(
        name = "Betsanddice-craps",
        description = "Schema to hold the result of a bet"
)
@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BetDto {

    @JsonProperty(value = "player_result", index = 0)
    private int playerResult;

    @JsonProperty(value = "amount_bet", index = 1)
    private BigDecimal amountBet;

    @JsonProperty(value = "player_wins", index = 2)
    private boolean playerWins = true;

    @JsonProperty(value = "amount_won", index = 3)
    private BigDecimal amountWon;
}
