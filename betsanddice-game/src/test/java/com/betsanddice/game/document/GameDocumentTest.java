package com.betsanddice.game.document;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GameDocumentTest {

    @Test
    void getUuidTest() {
        UUID uuid = UUID.randomUUID();
        GameDocument gameDocument = new GameDocument(uuid, null, null, null,
                null);
        assertEquals(uuid, gameDocument.getUuid());
    }

    @Test
    void getGameIdTest() {
        UUID uuid = UUID.randomUUID();
        GameDocument gameDocument = new GameDocument(null, uuid, null, null,
                null);
        assertEquals(uuid, gameDocument.getGameId());
    }

    @Test
    void getGameNameTest() {
        String gameName = "gameName";
        GameDocument gameDocument = new GameDocument(null, null, gameName, null,
                null);
        assertEquals(gameName, gameDocument.getGameName());
    }

    @Test
    void getTutorialIdTest() {
        UUID uuid = UUID.randomUUID();
        GameDocument gameDocument = new GameDocument(null, null, null, uuid,
                null);
        assertEquals(uuid, gameDocument.getTutorialId());
    }

    @Test
    void getStatIdTest() {
        UUID uuid = UUID.randomUUID();
        GameDocument gameDocument = new GameDocument(null, null, null, null,
                uuid);
        assertEquals(uuid, gameDocument.getStatId());
    }
}