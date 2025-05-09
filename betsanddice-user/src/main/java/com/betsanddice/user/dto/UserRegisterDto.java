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

//    @JsonProperty(value = "name")
//    @NotBlank(message = "Name can't be empty.")
//    @Size(min = 2, message = "Invalid Name: name should have at least 2 characters.")
//    private String name;
//
//    @JsonProperty(value = "surname")
//    @NotBlank(message = "Surname can't be empty." )
//    @Size(min = 2, message = "Invalid Surname: surname should have at least 2 characters")
//    private String surname;

    @JsonProperty(value = "nickname")
    @NotBlank(message = "Nickname can't be empty.")
    @Size(min = 3, max = 20,  message = "Invalid Nickname: Nickname should have at least 2 characters and not more than 20")
    private String nickname;

    @JsonProperty(value = "email")
    @NotBlank(message = "Email cannot be empty")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid Email: email should be in a valid format")
    private String email;

    @JsonProperty(value = "password")
    @NotBlank(message = "Password can't be empty.")
    @Size(min = 5, message = "Invalid Password: password should have at least 5 characters")
    private String password;

    @JsonProperty(value = "birthdate")
    @NotNull(message = "Birthdate can't be empty.")
    @Past(message = "Invalid Birthdate: birthdate should be prior to the current one.")
    private LocalDate birthdate;
}