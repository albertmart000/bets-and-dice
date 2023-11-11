package com.betsanddice.stat.dto;

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
public class UserStatDto {

    @JsonProperty(value = "userStat_id", index = 0)
    private UUID uuid;

    @JsonProperty(value = "userid", index = 1)
    private UUID userId;

    @JsonProperty(value = "gameId", index = 2)
    private UUID gameId;

    @JsonProperty(value = "average", index = 3)
    private double average;
}
