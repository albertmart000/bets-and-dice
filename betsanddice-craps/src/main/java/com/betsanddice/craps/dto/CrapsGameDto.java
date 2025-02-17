package com.betsanddice.craps.dto;

import com.betsanddice.craps.document.BetDocument;
import com.betsanddice.craps.document.DiceRollDocument;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Schema(
        name = "Betsanddice-craps",
        description = "Schema to hold Craps game information"
)
@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CrapsGameDto {

    @JsonProperty(value = "id_craps_game", index = 0)
    private UUID uuid;

    @JsonProperty(value = "id_user", index = 1)
    private UUID userId;

    @JsonProperty(value = "date", index = 2)
    private String date;

    @JsonProperty(value = "bet", index = 3)
    private BetDocument bet;

    @JsonProperty(value = "dice_rolls", index = 4)
    private List<DiceRollDocument> diceRollsList;

    @JsonProperty(value = "result_game", index = 5)
    private ResultDto result;
}