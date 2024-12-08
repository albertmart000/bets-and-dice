package com.betsanddice.craps.document;

import com.betsanddice.craps.dto.DiceRollDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;

class CrapsGameDocumentTest {

    private final UUID uuid = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();
    private final LocalDateTime date = now();
    private final List<DiceRollDto> diceRollsList = List.of(
            new DiceRollDto(1, 2, 3),
            new DiceRollDto(3, 4, 7)
    );

    @Test
    void testAllArgsConstructor() {
        CrapsGameDocument document = new CrapsGameDocument(uuid, userId, date, diceRollsList);

        assertEquals(uuid, document.getUuid());
        assertEquals(userId, document.getUserId());
        assertEquals(date, document.getDate());
        assertEquals(diceRollsList, document.getDiceRollsList());
    }

    @Test
    void testNoArgsConstructor() {
        CrapsGameDocument document = new CrapsGameDocument();
        assertNotNull(document);
    }

    @Test
    void testBuilder() {
        CrapsGameDocument document = CrapsGameDocument.builder()
                .uuid(uuid)
                .userId(userId)
                .date(date)
                .diceRollsList(diceRollsList)
                .build();

        assertEquals(uuid, document.getUuid());
        assertEquals(userId, document.getUserId());
        assertEquals(date, document.getDate());
        assertEquals(diceRollsList, document.getDiceRollsList());
    }

    @Test
    void getUuidTest() {
        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(uuid, null,
                null, null);
        assertEquals(uuid, crapsGameDocument.getUuid());
    }

    @Test
    void getUserIdTest() {
        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(null, uuid,
                null, null);
        assertEquals(uuid, crapsGameDocument.getUserId());
    }

    @Test
    void getDateTest() {
        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(null, null,
                date, null);
        assertEquals(date, crapsGameDocument.getDate());
    }

    @Test
    void getDiceRollsTest() {
        CrapsGameDocument crapsGameDocument = new CrapsGameDocument(null, null,
                null, diceRollsList);
        assertEquals(diceRollsList, crapsGameDocument.getDiceRollsList());
    }

}