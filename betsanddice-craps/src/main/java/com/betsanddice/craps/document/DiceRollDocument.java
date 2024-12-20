package com.betsanddice.craps.document;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiceRollDocument {

    @Field(name = "dice_1")
    private Integer dice1;

    @Field(name = "dice_2")
    private Integer dice2;
}
