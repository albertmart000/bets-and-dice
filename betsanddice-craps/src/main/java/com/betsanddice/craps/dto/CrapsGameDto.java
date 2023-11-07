package com.betsanddice.craps.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CrapsGameDto {

    @JsonProperty(value = "crapsGame_id", index = 0)
    private UUID uuid;

    @JsonProperty(value = "userid", index = 1)
    private UUID userId;

    @JsonProperty(value = "user_nickname", index = 2)
    private String userNickname;

    @JsonProperty(value = "date", index = 3)
    private String date;

    @JsonProperty(value = "dice_rolls", index = 4)
    private int[][] diceRolls = new int[2][];
}