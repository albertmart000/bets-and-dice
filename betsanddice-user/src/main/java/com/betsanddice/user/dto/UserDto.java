package com.betsanddice.user.dto;

import com.betsanddice.user.annotations.ValidUUID;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Schema(
        name = "Betsanddice-user",
        description = "Schema to hold User information"
)
@Component
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonPropertyOrder({"user_id", "name", "surname", "birthdate", "nickname", "email", "password"})
public class UserDto {

    @JsonProperty(value = "user_id")
    @ValidUUID(message = "Invalid ID format. Please indicate the correct format.")
    private UUID uuid;

    @JsonProperty(value = "name")
    @NotBlank(message = "Invalid Name: Name can't be empty.")
    @NotNull(message = "Invalid Name: Name can't be NULL.")
    @Size(min = 2, message = "Invalid Name: Name should have at least 2 characters.")
    private String name;

    @JsonProperty(value = "surname")
    @NotBlank(message = "Invalid Surname: Surname can't be empty." )
    @NotNull(message = "Invalid Surname: Surname can't be NULL.")
    @Size(min = 2, message = "Invalid Surname: Surname should have at least 2 characters")
    private String surname;

    @JsonProperty(value = "birthdate")
    private String birthdate;

    @JsonProperty(value = "nickname")
    @NotBlank(message = "Invalid Nickname: Surname can't be empty." )
    @NotNull(message = "Invalid Nickname: Surname can't be NULL.")
    @Size(min = 2, message = "Invalid Nickname: Nickname should have at least 2 characters")
    private String nickname;

    @JsonProperty(value = "email")
    @Email
    private String email;

    @JsonProperty(value = "password")
    @NotBlank(message = "Invalid Password: Password can't be empty." )
    @NotNull(message = "Invalid Password: Password can't be NULL.")
    @Size(min = 2, message = "Invalid Password: Password should have at least 2 characters")
    private String password;

}
