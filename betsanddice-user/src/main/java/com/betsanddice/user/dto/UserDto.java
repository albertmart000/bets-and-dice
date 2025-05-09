package com.betsanddice.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Schema(
        name = "User",
        description = "Schema to display User information"
)
@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonPropertyOrder({"user_id", "nickname", "email", "password", "birthdate","registration_date"})
public class UserDto {

    @JsonProperty(value = "user_id")
    private UUID uuid;

//    @JsonProperty(value = "name")
//    private String name;
//
//    @JsonProperty(value = "surname")
//    private String surname;

    @JsonProperty(value = "nickname")
    private String nickname;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(value = "registration_date")
    private String registrationDate;

    @JsonProperty(value = "birthdate")
    private String birthdate;
    //TODO: Add authorities and date

}