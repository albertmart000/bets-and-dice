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
    void getDateTest() {
        LocalDateTime date = now();
        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(null, null,
                 date, null, null);
        assertEquals(date, crapsGameDocument.getDate());
    }

    @Test
    void getAttemptsTest() {
        int attempts = 2;
        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(null, null,
                null, 2, null);
        assertEquals(attempts, crapsGameDocument.getAttempts());
    }

    @Test
    void getDiceRollsTest() {
        DiceRollDocument diceRollDocument1= new DiceRollDocument( 1, 2, 3);
        DiceRollDocument diceRollDocument2= new DiceRollDocument( 3, 4, 7);
        List<DiceRollDocument> diceRollsList = List.of(diceRollDocument1, diceRollDocument2);

        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(null, null,
                null, null, diceRollsList);
        assertEquals(diceRollsList, crapsGameDocument.getDiceRollsList());
    }

}