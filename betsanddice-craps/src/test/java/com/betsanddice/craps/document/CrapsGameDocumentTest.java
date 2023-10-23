package com.betsanddice.craps.document;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CrapsGameDocumentTest {
    @Test
    void getUuidTest() {
        UUID uuid = UUID.randomUUID();
        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(uuid, null,
                null, null);
        assertEquals(uuid, crapsGameDocument.getUuid());
    }

    @Test
    void getUserIdTest() {
        UUID uuid = UUID.randomUUID();
        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(null, uuid,
                null, null);
        assertEquals(uuid, crapsGameDocument.getUserId());
    }
    @Test
    void getUserNickNameTest() {
        String userNickName = "userNickName";
        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(null, null,
                userNickName, null);
        assertEquals(userNickName, crapsGameDocument.getUserNickname());
    }

    @Test
    void getDiceRollsTest() {
        int[][] diceRolls = {{1, 2}, {3, 4}, {5, 6}};
        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(null, null,
                null, diceRolls);
        assertEquals(diceRolls, crapsGameDocument.getDiceRolls());
    }

}