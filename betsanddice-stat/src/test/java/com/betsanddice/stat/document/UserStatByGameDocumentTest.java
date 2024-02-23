package com.betsanddice.stat.document;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserStatByGameDocumentTest {

    @Test
    void getUuidTest() {
        UUID uuid = UUID.randomUUID();
        UserStatByGameDocument userStatByGameDocument = new UserStatByGameDocument(uuid, null, null);

        assertEquals(uuid, userStatByGameDocument.getUuid());
    }

    @Test
    void getUserIdTest() {
        UUID uuid = UUID.randomUUID();
        UserStatByGameDocument userStatByGameDocument = new UserStatByGameDocument(null, uuid, null);

        assertEquals(uuid, userStatByGameDocument.getUserId());
    }
    @Test
    void getGameIdTest() {
        UUID uuid = UUID.randomUUID();
        UserStatByGameDocument userStatByGameDocument = new UserStatByGameDocument(null, null, uuid);

        assertEquals(uuid, userStatByGameDocument.getGameId());
    }

}