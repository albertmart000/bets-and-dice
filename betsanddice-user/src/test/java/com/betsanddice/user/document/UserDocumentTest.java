package com.betsanddice.user.document;

import com.betsanddice.user.document.enums.Role;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDocumentTest {
    @Test
    void getUuidTest() {
        UUID uuid = UUID.randomUUID();
        UserDocument userDocument = new UserDocument(uuid, null, null, null, null,
                null, null, null, null);
        assertEquals(uuid, userDocument.getUuid());
    }

    @Test
    void getNameTest() {
        String name = "name";
        UserDocument userDocument = new UserDocument(null, name, null, null, null,
                null, null, null, null);
        assertEquals(name, userDocument.getFirstName());
    }

    @Test
    void getSurNameTest() {
        String surname = "surname";
        UserDocument userDocument = new UserDocument(null, null, surname, null, null,
                null, null, null, null);
        assertEquals(surname, userDocument.getSurname());
    }

    @Test
    void getNickNameTest() {
        String nickname = "nickname";
        UserDocument userDocument = new UserDocument(null, null, null, nickname, null,
                null, null, null, null);
        assertEquals(nickname, userDocument.getNickname());
    }

    @Test
    void getEmailTest() {
        String email = "email";
        UserDocument userDocument = new UserDocument(null, null, null, null, email,
                null, null, null, null);
        assertEquals(email, userDocument.getEmail());
    }

    @Test
    void getPasswordTest() {
        String password = "password";
        UserDocument userDocument = new UserDocument(null, null, null, null, null,
                password, null, null, null);
        assertEquals(password, userDocument.getPassword());
    }

    @Test
    void getBirthdateTest() {
        LocalDate birthdate = LocalDate.now();
        UserDocument userDocument = new UserDocument(null, null, null, null, null,
                null, birthdate, null, null);
        assertEquals(birthdate, userDocument.getBirthdate());
    }

    @Test
    void getRegisteredTest() {
        LocalDateTime registrationDate = now();
        UserDocument userDocument = new UserDocument(null, null, null, null, null,
                null, null, registrationDate, null);
        assertEquals(registrationDate, userDocument.getRegistrationDate());
    }

    @Test
    void getRoleTest() {
        Role role = Role.PLAYER;
        UserDocument userDocument = new UserDocument(null, null, null, null, null,
                null, null, null, role);
        assertEquals(role, userDocument.getRole());
    }
}