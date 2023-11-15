package com.betsanddice.craps.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
@Document(collection = "database_sequences")

public class DatabaseSequence {

    @Id
    private String id;
    private long seq;

}
