package com.betsanddice.craps.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DiceRollDtoTest {

    @Test
    void testNoArgsConstructor() {
        DiceRollDto dto = new DiceRollDto();
        assertNull(dto.getDice1());
        assertNull(dto.getDice2());
        assertNull(dto.getResult());
    }

    @Test
    void testAllArgsConstructor() {
        Integer dice1 = 1;
        Integer dice2 = 2;
        Integer result = 3;
        DiceRollDto dto = new DiceRollDto(dice1, dice2, result);
        assertEquals(dice1, dto.getDice1());
        assertEquals(dice2, dto.getDice2());
        assertEquals(result, dto.getResult());
    }

    @Test
    void testSettersAndGetters() {
        DiceRollDto dto = new DiceRollDto();
        Integer dice1 = 1;
        Integer dice2 = 2;
        Integer result = 3;
        dto.setDice1(dice1);
        dto.setDice2(dice2);
        dto.setResult(result);
        assertEquals(dice1, dto.getDice1());
        assertEquals(dice2, dto.getDice2());
        assertEquals(result, dto.getResult());
    }
}