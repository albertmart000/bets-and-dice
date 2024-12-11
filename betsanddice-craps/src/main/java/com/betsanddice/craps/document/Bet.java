package com.betsanddice.craps.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "bets")
public class Bet {
    @Id
    @Field(name = "_id")
    private UUID uuid;

    @Field(name = "id_user")
    private UUID userId;

    @Field(name = "id_game")
    private UUID gameId;

    @Field(name = "amount_wagered")
    private BigDecimal amountWagered;
}
