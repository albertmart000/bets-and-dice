package com.betsanddice.craps.document;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class DiceRollsDocumentTest {
    @Test
    void getUuidTest() {
        UUID uuid = UUID.randomUUID();
        DiceRollDocument diceRollsDocument = new DiceRollDocument(uuid, null, null,
                null, null);
        assertEquals(uuid, diceRollsDocument.getUuid());
    }

    @Test
    void getCrapsGameUuidTest() {
        UUID uuid = UUID.randomUUID();
        DiceRollDocument diceRollsDocument = new DiceRollDocument(null, uuid,
                null, null, null);
        assertEquals(uuid, diceRollsDocument.getCrapsGameUuid());
    }

    @Test
    void getDice1Test() {
        Integer dice1 = 1;
        DiceRollDocument diceRollsDocument = new DiceRollDocument(null, null,
                1, null, null);
        assertEquals(dice1, diceRollsDocument.getDice1());
    }

    @Test
    void getDice2Test() {
        Integer dice2 = 2;
        DiceRollDocument diceRollsDocument = new DiceRollDocument(null, null,
                null, 2, null);
        assertEquals(dice2, diceRollsDocument.getDice2());
    }

    @Test
    void getResultTest() {
        Integer result = 3;
        DiceRollDocument diceRollsDocument = new DiceRollDocument(null, null,
                null, null, 3);
        assertEquals(result, diceRollsDocument.getResult());

    }
}