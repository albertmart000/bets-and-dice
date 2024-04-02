package com.betsanddice.stat.document;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserGameStatDocumentTest {

    @Test
    void getUuidTest() {
        UUID uuid = UUID.randomUUID();
        UserGameStatDocument userGameStatDocument = new UserGameStatDocument(uuid, null, null, 0);

        assertEquals(uuid, userGameStatDocument.getUuid());
    }

    @Test
    void getUserIdTest() {
        UUID uuid = UUID.randomUUID();
        UserGameStatDocument userGameStatDocument = new UserGameStatDocument(null, uuid, null, 0);

        assertEquals(uuid, userGameStatDocument.getUserId());
    }

    @Test
    void getGameIdTest() {
        UUID uuid = UUID.randomUUID();
        UserGameStatDocument userGameStatDocument = new UserGameStatDocument(null, null, uuid, 0);

        assertEquals(uuid, userGameStatDocument.getGameId());
    }

    @Test
    void getAverageTest() {
        double average = 50.0;
        UserGameStatDocument userGameStatDocument = new UserGameStatDocument(null, null, null, average);

        assertEquals(average, userGameStatDocument.getAverage());
    }

}