package com.betsanddice.user.document;

import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users")
public class UserDocument {

    @MongoId
    @Field(name = "_id")
    private UUID uuid;

    @Field(name = "name")
    private String name;

    @Field(name = "surname")
    private String surname;

    @Field(name = "nickname")
    private String nickname;

    @Email
    @Field(name = "email")
    private String email;

    @Field(name = "password")
    private String password;

    @Field(name="registration_date")
    private LocalDateTime registrationDate;

    @Field(name = "games")
    private List<UUID> games;

    @Field(name = "stadistics")
    private List<Role> stadistics;

    @Field(name = "role")
    private List<Role> roles;

}
