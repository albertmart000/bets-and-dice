package com.betsanddice.stat.document;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserStatDocumentTest {

    @Test
    void getUuidTest() {
        UUID uuid = UUID.randomUUID();
        UserStatDocument userStatDocument = new UserStatDocument(uuid, null, null, 0, null);

        assertEquals(uuid, userStatDocument.getUuid());
    }

    @Test
    void getUserIdTest() {
        UUID uuid = UUID.randomUUID();
        UserStatDocument userStatDocument = new UserStatDocument(null, uuid, null, 0, null);

        assertEquals(uuid, userStatDocument.getUserId());
    }

    @Test
    void getLevelTest() {
        String level = "Intermediate";
        UserStatDocument userStatDocument = new UserStatDocument(null, null, "Intermediate", 0, null);

        assertEquals(level, userStatDocument.getLevel());
    }

    @Test
    void getCashTest() {
        int cash = 50;
        UserStatDocument userStatDocument = new UserStatDocument(null, null, null, 50, null);

        assertEquals(cash, userStatDocument.getCash());
    }

    @Test
    void getGamesTest() {
        UUID uuid = UUID.randomUUID();
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UserStatByGameDocument userStatByGameDocument = new UserStatByGameDocument(uuid, uuid1, uuid2);
        List<UserStatByGameDocument> userGames = List.of(userStatByGameDocument);
        UserStatDocument userStatDocument = new UserStatDocument(null, null, null, 0, userGames);

        assertEquals(userGames, userStatDocument.getUserGames());
    }

}