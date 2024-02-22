package com.betsanddice.stat.dto;

import com.betsanddice.stat.document.UserStatByGameDocument;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserStatDto {

    @JsonProperty(value = "user_stat_id", index = 0)
    private UUID uuid;

    @JsonProperty(value = "user_id", index = 1)
    private UUID userId;

    @JsonProperty(value = "user_nickname", index = 2)
    private String userNickname;

    @JsonProperty(value = "level", index = 3)
    private String level;

    @JsonProperty(value = "cash", index = 4)
    private BigDecimal cash;

    @JsonProperty(value = "ranking_by_winnings", index = 5)
    private double rankingByWinnings;

    @JsonProperty(value = "user_games", index = 6)
    private List<UserStatByGameDocument> userGames;

}
