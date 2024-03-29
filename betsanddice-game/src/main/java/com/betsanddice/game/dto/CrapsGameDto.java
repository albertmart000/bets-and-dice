package com.betsanddice.game.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

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

    @JsonProperty(value = "attempts", index = 3)
    private Integer attempts;

    @JsonProperty(value = "dice_rolls", index = 4)
    private List<DiceRollDto> diceRollsList;

}
