package com.betsanddice.craps.dto;

import com.betsanddice.craps.document.DiceRollDocument;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({"id_craps_game", "id_user", "date", "bet", "dice_rolls", "result_game"})
public class CrapsGameDto {

    @JsonProperty(value = "id_craps_game")
    private UUID uuid;

    @JsonProperty(value = "id_user")
    private UUID userId;

    @JsonProperty(value = "date")
    private String date;

    @JsonProperty(value = "bet")
    private BetDto bet;

    @JsonProperty(value = "dice_rolls")
    private List<DiceRollDocument> diceRollsList;

    @JsonProperty(value = "result")
    private ResultDto result;
}