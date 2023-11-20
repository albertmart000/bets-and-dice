package com.betsanddice.craps.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiceRollDto {

/*    @JsonProperty(value = "id_dice_roll", index = 0)
    private UUID uuid;*/

/*  @Field(name = "id_craps_game")
    private UUID crapsGameUuid;*/

    @JsonProperty(value = "dice_1", index = 1)
    private Integer dice1;

    @JsonProperty(value = "dice_2", index = 2)
    private Integer dice2;

    @JsonProperty(value = "result", index = 3)
    private Integer result;

/*    public DiceRollDto(Integer dice1, Integer dice2, Integer result) {
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.result = result;
    }*/

}
