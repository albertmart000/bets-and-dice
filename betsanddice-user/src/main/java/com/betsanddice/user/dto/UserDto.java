package com.betsanddice.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

    @JsonProperty(value = "birthdate", index = 2)
    private String birthdate;

    @JsonProperty(value = "nickname", index = 3)
    private String nickname;

    @JsonProperty(value = "email", index = 4)
    private String email;

    @JsonProperty(value = "registered", index = 5)
    private String registrationDate;

}
