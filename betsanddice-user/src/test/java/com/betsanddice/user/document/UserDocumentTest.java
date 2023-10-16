package com.betsanddice.user.document;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDocumentTest {
    @Test
    void getUuid() {
        UUID uuid = UUID.randomUUID();
        UserDocument userDocument = new UserDocument(uuid, null, null, null, null,
                null, null, null, null, null, null, null);
        assertEquals(uuid, userDocument.getUuid());
    }

    @Test
    void getName() {
        String name = "name";
        UserDocument userDocument = new UserDocument(null, name, null, null, null,
                null, null, null, null, null, null, null);
        assertEquals(name, userDocument.getFirstName());
    }

    @Test
    void geSurName() {
        String surname = "surname";
        UserDocument userDocument = new UserDocument(null, null, surname, null, null,
                null, null, null, null, null, null, null);
        assertEquals(surname, userDocument.getSurname());
    }

    @Test
    void getBirthdate() {
        LocalDate birthdate = LocalDate.now();

        UserDocument userDocument = new UserDocument(null, null, null, birthdate, null,
                null, null, null, null, null, null, null);
        assertEquals(birthdate, userDocument.getBirthdate());
    }

    @Test
    void getNickName() {
        String nickname = "nickname";
        UserDocument userDocument = new UserDocument(null, null, null, null, nickname,
                null, null, null, null, null, null, null);
        assertEquals(nickname, userDocument.getNickname());
    }

    @Test
    void getEmail() {
        String email = "email";
        UserDocument userDocument = new UserDocument(null, null, null, null, null,
                email, null, null, null, null, null, null);
        assertEquals(email, userDocument.getEmail());
    }

    @Test
    void getPassword() {
        String password = "password";
        UserDocument userDocument = new UserDocument(null, null, null, null, null,
                null, password, null, null, null, null, null);
        assertEquals(password, userDocument.getPassword());
    }

    @Test
    void getRegistered() {
        LocalDateTime registrationDate = now();
        UserDocument userDocument = new UserDocument(null, null, null, null, null,
                null, null, registrationDate, null, null, null, null);
        assertEquals(registrationDate, userDocument.getRegistrationDate());
    }

    @Test
    void getLevel() {
        String level = "level";
        UserDocument userDocument = new UserDocument(null, null, null, null, null,
                null, null, null, level, null, null, null);
        assertEquals(level, userDocument.getLevel());
    }

    @Test
    void getGames() {
        UUID uuidGame1 = UUID.fromString("dcacb291-b4aa-4029-8e9b-284c8ca80296");
        UUID uuidGame2 = UUID.fromString("09fabe32-7362-4bfb-ac05-b7bf854c6e0f");
        List<UUID> gameList = List.of(uuidGame1, uuidGame2);
        UserDocument userDocument = new UserDocument(null, null, null, null, null,
                null, null, null, null, null, gameList, null);
        assertEquals(gameList, userDocument.getGames());
    }

    @Test
    void getStatistics() {
        UUID uuidStatistics1 = UUID.fromString("70e9755e-9e83-41d3-853a-665f1f2a2f5c");
        UUID uuidStatistics2 = UUID.fromString("bb7897b8-517d-4843-8c17-e347aba086ca");
        List<UUID> statisticsList = List.of(uuidStatistics1, uuidStatistics2);
        UserDocument userDocument = new UserDocument(null, null, null, null, null,
                null,null, null, null, null, null, statisticsList);
        assertEquals(statisticsList, userDocument.getStatistics());
    }
}