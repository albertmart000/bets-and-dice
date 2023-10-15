package com.betsanddice.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
    @NotBlank(message = "Invalid Name: Name can't be empty.")
    @NotNull(message = "Invalid Name: Name can't be NULL.")
    @Size(min = 2, message = "Invalid Name: Name should have at least 2 characters.")
    private String name;

    @JsonProperty(value = "surname", index = 2)
    @NotBlank(message = "Invalid Surname: Surname can't be empty." )
    @NotNull(message = "Invalid Surname: Surname can't be NULL.")
    @Size(min = 2, message = "Invalid Surname: Surname should have at least 2 characters")
    private String surname;

    @JsonProperty(value = "birthdate", index = 3)
    private String birthdate;

    @JsonProperty(value = "nickname", index = 4)
    @NotBlank(message = "Invalid Nickname: Surname can't be empty." )
    @NotNull(message = "Invalid Nickname: Surname can't be NULL.")
    @Size(min = 2, message = "Invalid Nickname: Nickname should have at least 2 characters")
    private String nickname;

    @JsonProperty(value = "email", index = 5)
    @Email
    private String email;

    @JsonProperty(value = "password", index = 6)
    @NotBlank(message = "Invalid Password: Password can't be empty." )
    @NotNull(message = "Invalid Password: Password can't be NULL.")
    @Size(min = 2, message = "Invalid Password: Password should have at least 2 characters")
    private String password;

    @JsonProperty(value = "registered", index = 7)
    private String registrationDate;

    @JsonProperty(value = "cash", index = 8)
    private BigDecimal cash;

    @JsonProperty(value = "games", index = 9)
    private List<UUID> games;

    @JsonProperty(value = "statistics", index = 10)
    private List<UUID> statistics;

}
