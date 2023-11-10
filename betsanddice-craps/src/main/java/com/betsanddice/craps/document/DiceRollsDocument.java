package com.betsanddice.craps.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "diceRolls")
public class DiceRollsDocument {

    @Id
    @Field(name = "_id")
    private UUID uuid;

    @Field(name = "crapsGameUuid")
    private UUID crapsGameUuid;

    @Field(name = "dice1")
    private Integer dice1;

    @Field(name = "dice2")
    private Integer dice2;

}
