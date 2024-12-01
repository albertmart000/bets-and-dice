package com.betsanddice.craps.dto;

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

    @JsonProperty(value = "id_user", index = 0)
    private UUID userId;

    @JsonProperty(value = "attempts", index = 1)
    private Integer attempts;

    @JsonProperty(value = "dice_rolls", index = 2)
    private List<DiceRollDto> diceRollsList;

}
