package com.betsanddice.craps.document;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
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
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        List<UUID> diceRollsList = List.of(uuid1, uuid2);

        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(null, null,
                null, null, diceRollsList);
        assertEquals(diceRollsList, crapsGameDocument.getDiceRollsList());
    }

}