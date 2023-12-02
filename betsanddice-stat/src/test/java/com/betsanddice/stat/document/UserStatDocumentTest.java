package com.betsanddice.stat.document;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserStatDocumentTest {

    @Test
    void getUuidTest() {
        UUID uuid = UUID.randomUUID();
        UserStatDocument userStatDocument = new UserStatDocument(uuid, null, null, 0);

        assertEquals(uuid, userStatDocument.getUuid());
    }

    @Test
    void getUserIdTest() {
        UUID uuid = UUID.randomUUID();
        UserStatDocument userStatDocument = new UserStatDocument(null, uuid, null, 0);

        assertEquals(uuid, userStatDocument.getUserId());
    }

    @Test
    void getGameIdTest() {
        UUID uuid = UUID.randomUUID();
        UserStatDocument userStatDocument = new UserStatDocument(null, null, uuid, 0);

        assertEquals(uuid, userStatDocument.getGameId());
    }

    @Test
    void getAverageTest() {
        double average = 50.0;
        UserStatDocument userStatDocument = new UserStatDocument(null, null, null, average);

        assertEquals(average, userStatDocument.getAverage());
    }

}