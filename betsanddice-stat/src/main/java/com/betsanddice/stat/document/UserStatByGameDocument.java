package com.betsanddice.stat.document;

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
@Document(collection = "user-stats-by-game")
public class UserStatByGameDocument {

    @Id
    @Field(name = "_id")
    private UUID uuid;

    @Field(name = "user_id")
    private UUID userId;

    @Field(name = "game_id")
    private UUID gameId;
}
