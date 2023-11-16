package com.betsanddice.craps.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "dice-rolls")
public class DiceRollDocument {

    @Id
    @Field(name = "_id")
    private UUID uuid;

    @Field(name = "id_craps_game")
    private UUID crapsGameUuid;

    @Field(name = "dice_1")
    private Integer dice1;

    @Field(name = "dice_2")
    private Integer dice2;

    @Field(name = "result")
    private Integer result;

    public DiceRollDocument(Integer dice1, Integer dice2, Integer result) {
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.result = result;
    }
}
