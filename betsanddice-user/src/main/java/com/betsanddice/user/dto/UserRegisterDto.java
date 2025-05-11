package com.betsanddice.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Schema(
        name = "UserRegister",
        description = "Schema to register User information"
)
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserRegisterDto {

    @JsonProperty(value = "name")
    @Size(min = 3, max = 20, message = "Invalid Name: name can't be empty and should have at least 3 characters and not more than 20.")
    private String name;

    @JsonProperty(value = "surname")
    @Size(min = 3, max = 20, message = "Invalid Surname: Surname can't be empty and should have at least 3 characters and not more than 20.")
    private String surname;

    @JsonProperty(value = "nickname")
    @Size(min = 3, max = 20,  message = "Invalid Nickname: Nickname should have at least 3 characters and not more than 20")
    private String nickname;

    @JsonProperty(value = "email")
    @NotBlank(message = "Email cannot be empty")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid Email: email should be in a valid format")
    private String email;

    @JsonProperty(value = "password")
    @Size(min = 5, message = "Invalid Password: password  can't be empty and should have at least 8 characters and not more than 20.")
    private String password;

    @JsonProperty(value = "birthdate")
    @NotNull(message = "Birthdate can't be empty.")
    @Past(message = "Invalid Birthdate: birthdate should be prior to the current one.")
    private LocalDate birthdate;

}