package com.betsanddice.game.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
@Document(collection = "database_sequences")
public class DatabaseSequence {

    @Id
    @Field(name = "_id")
    private String id;

    @Field(name = "sequence")
    private long seq;
}
