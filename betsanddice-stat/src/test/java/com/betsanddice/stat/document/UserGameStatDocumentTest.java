package com.betsanddice.stat.document;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserGameStatDocumentTest {

    @Test
    void getUuidTest() {
        UUID uuid = UUID.randomUUID();
        UserGameStatDocument userGameStatDocument = new UserGameStatDocument(uuid, null, null,
                null, 0, 0);
        assertEquals(uuid, userGameStatDocument.getUuid());
    }

    @Test
    void getUserIdTest() {
        UUID uuid = UUID.randomUUID();
        UserGameStatDocument userGameStatDocument = new UserGameStatDocument(null, uuid, null,
                null, 0, 0);
        assertEquals(uuid, userGameStatDocument.getUserId());
    }

    @Test
    void getGameIdTest() {
        UUID uuid = UUID.randomUUID();
        UserGameStatDocument userGameStatDocument = new UserGameStatDocument(null, null, uuid,
                null, 0, 0);
        assertEquals(uuid, userGameStatDocument.getGameId());
    }

    @Test
    void getGameNameTest() {
        String name = "name";
        UserGameStatDocument userGameStatDocument = new UserGameStatDocument(null, null, null,
                "name", 0, 0);
        assertEquals(name, userGameStatDocument.getGameName());
    }

    @Test
    void getGamesPlayedTest() {
        int gamesPlayed = 50;
        UserGameStatDocument userGameStatDocument = new UserGameStatDocument(null, null, null,
                null, 50, 0);
        assertEquals(gamesPlayed, userGameStatDocument.getGamesPlayed());
    }

    @Test
    void getGamesWonOrAttemptsTest() {
        int gamesWonOrAttempts = 10;
        UserGameStatDocument userGameStatDocument = new UserGameStatDocument(null, null, null,
                null, 0, 10);
        assertEquals(gamesWonOrAttempts, userGameStatDocument.getGamesWonOrAttempts());
    }
}