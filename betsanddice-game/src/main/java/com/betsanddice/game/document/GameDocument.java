package com.betsanddice.game.document;

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
@Document(collection="games")
public class GameDocument {

    @Id
    @Field(name = "_id")
    private UUID uuid;

    @Field(name = "name")
    private String gameName;

    @Field(name = "id_tutorial")
    private UUID tutorialId;

    @Field(name = "id_stat")
    private UUID statId;
}
