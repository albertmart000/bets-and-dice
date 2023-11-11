package com.betsanddice.craps.document;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DiceRollsDocumentTest {
    @Test
    void getUuidTest() {
        UUID uuid = UUID.randomUUID();
        DiceRollsDocument diceRollsDocument = new DiceRollsDocument(uuid, null,
                null, null);
        assertEquals(uuid, diceRollsDocument.getUuid());
    }

    @Test
    void getCrapsGameUuidTest() {
        UUID uuid = UUID.randomUUID();
        DiceRollsDocument diceRollsDocument = new DiceRollsDocument(null, uuid,
                null, null);
        assertEquals(uuid, diceRollsDocument.getCrapsGameUuid());
    }

    @Test
    void getDice1Test() {
        Integer dice1 = 1;
        DiceRollsDocument diceRollsDocument = new DiceRollsDocument(null, null,
                1, null);
        assertEquals(dice1, diceRollsDocument.getDice1());
    }

    @Test
    void getDice2Test() {
        Integer dice2 = 2;
        DiceRollsDocument diceRollsDocument = new DiceRollsDocument(null, null,
                null, 2);
        assertEquals(dice2, diceRollsDocument.getDice2());
    }

}