package com.betsanddice.craps.document;

import com.betsanddice.craps.dto.BetDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CrapsGameDocumentTest {

    private final UUID uuid = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();
    private final LocalDateTime date = now();
    private final BetDto betDto= new BetDto(7, 5, 100);

    private final List<DiceRollDocument> diceRollsList = List.of(
            new DiceRollDocument(1, 2),
            new DiceRollDocument(3, 4)
    );

    @Test
    void testAllArgsConstructor() {
        CrapsGameDocument document = new CrapsGameDocument(uuid, userId, date, betDto, diceRollsList);

        assertEquals(uuid, document.getUuid());
        assertEquals(userId, document.getUserId());
        assertEquals(date, document.getDate());
        assertEquals(betDto, document.getBet());
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
                .bet(betDto)
                .build();

        assertEquals(uuid, document.getUuid());
        assertEquals(userId, document.getUserId());
        assertEquals(date, document.getDate());
        assertEquals(betDto, document.getBet());
        assertEquals(diceRollsList, document.getDiceRollsList());
    }

    @Test
    void testSettersAndGetters() {
        CrapsGameDocument document = new CrapsGameDocument();
        document.setUuid(uuid);
        document.setUserId(userId);
        document.setDate(date);
        document.setBet(betDto);
        document.setDiceRollsList(diceRollsList);

        assertEquals(uuid, document.getUuid());
        assertEquals(userId, document.getUserId());
        assertEquals(date, document.getDate());
        assertEquals(betDto, document.getBet());
        assertEquals(diceRollsList, document.getDiceRollsList());
    }

}