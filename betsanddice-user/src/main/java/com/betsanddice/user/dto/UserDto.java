package com.betsanddice.user.dto;

import com.betsanddice.user.annotations.ValidUUID;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
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
    @NotBlank(message = "Name can't be empty.")
    @Size(min = 2, message = "Invalid Name: name should have at least 2 characters.")
    private String name;

    @JsonProperty(value = "surname")
    @NotBlank(message = "Surname can't be empty." )
    @Size(min = 2, message = "Invalid Surname: surname should have at least 2 characters")
    private String surname;

    @JsonProperty(value = "birthdate")
    @NotBlank(message = "Birthdate can't be empty." )
    @Past(message = "Invalid Birthdate: birthdate should be prior to the current one.")
    @Pattern(regexp = "yyyy-MM-dd", message = "Invalid Birthdate: birthdate should be in the format yyyy-MM-dd")
    private String birthdate;

    @JsonProperty(value = "nickname")
    @NotBlank(message = "Surname can't be empty." )
    @Size(min = 2, message = "Invalid Nickname: Nickname should have at least 2 characters")
    private String nickname;

    @JsonProperty(value = "email")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid Email: email should be a valid email address")
    private String email;

    @JsonProperty(value = "password")
    @NotBlank(message = "Password can't be empty." )
    @Size(min = 2, message = "Invalid Password: password should have at least 2 characters")
    private String password;

}
