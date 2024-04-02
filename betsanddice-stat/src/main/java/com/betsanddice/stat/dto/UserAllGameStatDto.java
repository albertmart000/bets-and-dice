package com.betsanddice.stat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserAllGameStatDto {

    @JsonProperty(value = "user_all_games_stat_id", index = 0)
    private UUID uuid;

    @JsonProperty(value = "user_id", index = 1)
    private UUID userId;

    @JsonProperty(index = 2)
    private List<UserGameStatDto> userGameStatDtoList;

}
