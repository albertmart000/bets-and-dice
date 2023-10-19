package com.betsanddice.craps.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "craps")
public class CrapsDocument {

    @Id
    @Field(name = "_id")
    private UUID uuid;

    @Field(name = "gameName")
    private String gameName;

    @Field(name = "rules")
    private String rules;

    private Map<UUID, List<UUID>> userGames;
}
