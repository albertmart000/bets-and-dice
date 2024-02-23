package com.betsanddice.tutorial.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "game-tutorials")
public class GameTutorialDocument {

    @Id
    @Field(name = "_id")
    private UUID uuid;

    @Field(name = "game_id")
    private UUID gameId;

    @Field(name = "game_name")
    private String gameName;

    @Field(name = "rules")
    private String rules;

}
