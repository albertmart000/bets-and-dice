package com.betsanddice.user.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class UserDocument {

    @Id
    @Field(name = "_id")
    private UUID uuid;

//    @Field(name = "name")
//    private String firstName;
//
//    @Field(name = "surname")
//    private String surname;

    @Field(name = "nickname")
    private String nickname;

    @Field(name = "email")
    private String email;

    @Field(name = "password")
    private String password;

    @Field(name = "birthdate")
    private LocalDate birthdate;

    @Field(name="registration_date")
    private LocalDateTime registrationDate;

}
