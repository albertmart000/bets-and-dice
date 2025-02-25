package com.betsanddice.craps.document;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceRollDocumentTest {

    @Test
    void testNoArgsConstructor() {
        DiceRollDocument diceRoll = new DiceRollDocument();
        assertNotNull(diceRoll);
        assertNull(diceRoll.getDice1());
        assertNull(diceRoll.getDice2());
    }


    @Test
    void testAllArgsConstructor() {
        DiceRollDocument diceRoll = new DiceRollDocument(1, 2);
        assertEquals(1, diceRoll.getDice1());
        assertEquals(2, diceRoll.getDice2());
    }

    @Test
    void testBuilder() {
        DiceRollDocument diceRoll = DiceRollDocument.builder()
                .dice1(1)
                .dice2(2)
                .build();

        assertEquals(1, diceRoll.getDice1());
        assertEquals(2, diceRoll.getDice2());
    }

    @Test
    void testSettersAndGetters() {
        DiceRollDocument diceRoll = new DiceRollDocument();
        diceRoll.setDice1(1);
        diceRoll.setDice2(2);

        assertEquals(1, diceRoll.getDice1());
        assertEquals(2, diceRoll.getDice2());
    }
}