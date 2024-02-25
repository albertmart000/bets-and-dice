package com.betsanddice.game.document;

import com.betsanddice.game.dto.DiceRollDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="craps-games")
public class CrapsGameDocument {

    @Id
    @Field(name = "_id")
    private UUID uuid;

    @Field(name = "id_user")
    private UUID userId;

    @Field(name = "date")
    private LocalDateTime date;

    @Field(name =  "attempts")
    private Integer attempts;

    @Field(name = "dice_rolls")
    private List<DiceRollDto> diceRollsList;

}
