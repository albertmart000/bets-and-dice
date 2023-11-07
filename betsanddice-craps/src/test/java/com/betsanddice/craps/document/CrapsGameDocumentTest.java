package com.betsanddice.craps.document;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CrapsGameDocumentTest {
    @Test
    void getUuidTest() {
        UUID uuid = UUID.randomUUID();
        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(uuid, null,
                null, null, null);
        assertEquals(uuid, crapsGameDocument.getUuid());
    }

    @Test
    void getUserIdTest() {
        UUID uuid = UUID.randomUUID();
        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(null, uuid,
                null, null, null);
        assertEquals(uuid, crapsGameDocument.getUserId());
    }
    @Test
    void getUserNickNameTest() {
        String userNickName = "userNickName";
        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(null, null,
                userNickName, null, null);
        assertEquals(userNickName, crapsGameDocument.getUserNickname());
    }
    @Test
    void getDateTest() {
        LocalDateTime date = now();
        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(null, null,
                null, date, null);
        assertEquals(date, crapsGameDocument.getDate());
    }

    @Test
    void getDiceRollsTest() {
        int[][] diceRolls = {{1, 2}, {3, 4}, {5, 6}};
        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(null, null,
                null, null, diceRolls);
        assertEquals(diceRolls, crapsGameDocument.getDiceRolls());
    }

}