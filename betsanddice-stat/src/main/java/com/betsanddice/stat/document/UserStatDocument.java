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
@Document(collection = "user-stats")
public class UserStatDocument {

    @Id
    @Field(name = "_id")
    private UUID uuid;

    @Field(name = "id_user")
    private UUID userId;

    @Field(name = "id_game")
    private UUID gameId;

    @Field(name = "average")
    private double average;
}
