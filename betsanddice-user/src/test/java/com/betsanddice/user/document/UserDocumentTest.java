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
    void getUuidTest() {
        UUID uuid = UUID.randomUUID();
        UserDocument userDocument = new UserDocument(uuid, null, null, null, null,
                null, null, null, null, null, null, null);
        assertEquals(uuid, userDocument.getUuid());
    }

    @Test
    void getNameTest() {
        String name = "name";
        UserDocument userDocument = new UserDocument(null, name, null, null, null,
                null, null, null, null, null, null, null);
        assertEquals(name, userDocument.getFirstName());
    }

    @Test
    void geSurNameTest() {
        String surname = "surname";
        UserDocument userDocument = new UserDocument(null, null, surname, null, null,
                null, null, null, null, null, null, null);
        assertEquals(surname, userDocument.getSurname());
    }

    @Test
    void getBirthdateTest() {
        LocalDate birthdate = LocalDate.now();

        UserDocument userDocument = new UserDocument(null, null, null, birthdate, null,
                null, null, null, null, null, null, null);
        assertEquals(birthdate, userDocument.getBirthdate());
    }

    @Test
    void getNickNameTest() {
        String nickname = "nickname";
        UserDocument userDocument = new UserDocument(null, null, null, null, nickname,
                null, null, null, null, null, null, null);
        assertEquals(nickname, userDocument.getNickname());
    }

    @Test
    void getEmailTest() {
        String email = "email";
        UserDocument userDocument = new UserDocument(null, null, null, null, null,
                email, null, null, null, null, null, null);
        assertEquals(email, userDocument.getEmail());
    }

    @Test
    void getPasswordTest() {
        String password = "password";
        UserDocument userDocument = new UserDocument(null, null, null, null, null,
                null, password, null, null, null, null, null);
        assertEquals(password, userDocument.getPassword());
    }

    @Test
    void getRegisteredTest() {
        LocalDateTime registrationDate = now();
        UserDocument userDocument = new UserDocument(null, null, null, null, null,
                null, null, registrationDate, null, null, null, null);
        assertEquals(registrationDate, userDocument.getRegistrationDate());
    }

    @Test
    void getLevelTest() {
        String level = "level";
        UserDocument userDocument = new UserDocument(null, null, null, null, null,
                null, null, null, level, null, null, null);
        assertEquals(level, userDocument.getLevel());
    }

    @Test
    void getGameTest() {
        UUID uuidGame1 = UUID.randomUUID();
        UUID uuidGame2 = UUID.randomUUID();
        List<UUID> gameList = List.of(uuidGame1, uuidGame2);
        UserDocument userDocument = new UserDocument(null, null, null, null, null,
                null, null, null, null, null, gameList, null);
        assertEquals(gameList, userDocument.getGames());
    }

    @Test
    void getStatisticsTest() {
        UUID uuidStatistics1 = UUID.randomUUID();
        UUID uuidStatistics2 = UUID.randomUUID();
        List<UUID> statisticsList = List.of(uuidStatistics1, uuidStatistics2);
        UserDocument userDocument = new UserDocument(null, null, null, null, null,
                null,null, null, null, null, null, statisticsList);
        assertEquals(statisticsList, userDocument.getStatistics());
    }
}