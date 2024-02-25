package com.betsanddice.game.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiceRollDto {

    @JsonProperty(value = "dice_1", index = 1)
    private Integer dice1;

    @JsonProperty(value = "dice_2", index = 2)
    private Integer dice2;

    @JsonProperty(value = "result", index = 3)
    private Integer result;

}
