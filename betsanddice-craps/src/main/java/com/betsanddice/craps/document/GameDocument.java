package com.betsanddice.craps.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.UUID;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "games")
public class GameDocument {

    @Id
    @Field(name = "_id")
    private UUID uuid;

    @Field(name = "userId")
    private UUID userId;

    @Field(name = "userNickname")
    private String userNickname;

    @Field(name = "diceRolls")
    private List<Integer> diceRolls;
}
