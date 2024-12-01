package com.betsanddice.craps.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.stereotype.Component;

@Schema(
        name = "Betsanddice-craps",
        description = "Schema to hold the result of a roll of two dice"
)
@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DiceRollDto {

    @JsonProperty(value = "dice_1", index = 0)
    private Integer dice1;

    @JsonProperty(value = "dice_2", index = 1)
    private Integer dice2;

    @JsonProperty(value = "result", index = 2)
    private Integer result;
}