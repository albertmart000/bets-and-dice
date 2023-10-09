package com.betsanddice.user.document;

import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
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
    private String firstName;

    @Field(name = "surname")
    private String surname;

    @Field(name = "nickname")
    private String nickname;

    @Email
    @Field(name = "email")
    private String email;

    @Field(name = "password")
    private String password;

    @Field(name="registered")
    private LocalDateTime registrationDate;

    @Field(name = "cash")
    private BigDecimal cash;

    @Field(name = "games")
    private List<UUID> games;

    @Field(name = "stadistics")
    private List<Role> stadistics;

    @Field(name = "role")
    private List<Role> roles;

}
