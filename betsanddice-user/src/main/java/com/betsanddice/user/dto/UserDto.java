package com.betsanddice.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    @JsonProperty(value = "surname", index = 2)
    private String surname;

    @JsonProperty(value = "birthdate", index = 3)
    private String birthdate;

    @JsonProperty(value = "nickname", index = 4)
    private String nickname;

    @JsonProperty(value = "email", index = 5)
    private String email;

    @JsonProperty(value = "password", index = 6)
    private String password;

    @JsonProperty(value = "registered", index = 7)
    private String registrationDate;

    @JsonProperty(value = "cash", index = 8)
    private BigDecimal cash;

    @JsonProperty(value = "games", index = 9)
    private List<UUID> games;

    @JsonProperty(value = "stadistics", index = 10)
    private List<UUID> stadistics;

}
