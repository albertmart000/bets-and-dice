package com.betsanddice.craps.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "crapsGames")
public class CrapsGameDocument {

    @Id
    @Field(name = "_id")
    private UUID uuid;

    @Field(name = "id_user")
    private UUID userId;

    @Field(name = "user_nickname")
    private String userNickname;

    @Field(name = "date")
    private LocalDateTime date;

    @Field(name = "dice_rolls")
    private List<DiceRollDocument> diceRollsList;

}
