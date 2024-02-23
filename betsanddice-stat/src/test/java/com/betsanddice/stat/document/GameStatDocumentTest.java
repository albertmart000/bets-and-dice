package com.betsanddice.stat.document;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameStatDocumentTest {

    @Test
    void getUuidTest() {
        UUID uuid = UUID.randomUUID();
        GameStatDocument gameStatDocument = new GameStatDocument(uuid, null);

        assertEquals(uuid, gameStatDocument.getUuid());
    }

    @Test
    void getGameIdTest() {
        UUID uuid = UUID.randomUUID();
        GameStatDocument gameStatDocument = new GameStatDocument(null, uuid);
        assertEquals(uuid, gameStatDocument.getGameId());
    }
}
