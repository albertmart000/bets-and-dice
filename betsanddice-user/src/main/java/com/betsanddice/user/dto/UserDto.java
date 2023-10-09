package com.betsanddice.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@Getter
public class UserDto {

    @JsonProperty(value = "user_id", index = 0)
    private UUID uuid;

    @JsonProperty(value = "name", index = 1)
    private String name;

    @JsonProperty(value = "nickname", index = 2)
    private String nickname;

    @JsonProperty(value = "email", index = 3)
    private String email;

    @JsonProperty(value = "registered", index = 4)
    private LocalDateTime registrationDate;

}