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
@Document(collection = "user-game-stats")
public class UserGameStatDocument {

    @Id
    @Field(name = "_id")
    private UUID uuid;

    @Field(name = "user_id")
    private UUID userId;

    @Field(name = "game_id")
    private UUID gameId;

    @Field(name = "game_name")
    private String gameName;

    @Field(name = "games_played")
    private int gamesPlayed;

    @Field(name = "games_won/attempts")
    private int gamesWonOrAttempts;
}
