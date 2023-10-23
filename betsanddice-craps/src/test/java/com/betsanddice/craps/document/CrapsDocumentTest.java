package com.betsanddice.craps.document;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CrapsDocumentTest {
    @Test
    void getUuidTest() {
        UUID uuid = UUID.randomUUID();
        CrapsDocument crapsDocument = new CrapsDocument(uuid, null,
                null, null);
        assertEquals(uuid, crapsDocument.getUuid());
    }

    @Test
    void getGameNameTest() {
        String gameName = "gameName";
        CrapsDocument crapsDocument = new CrapsDocument(null, gameName,
                null, null);
        assertEquals(gameName, crapsDocument.getGameName());
    }

    @Test
    void getRulesTest() {
        String rules = "rules";
        CrapsDocument crapsDocument = new CrapsDocument(null, null,
                rules, null);
        assertEquals(rules, crapsDocument.getRules());
    }

    @Test
    void getUserGamesTest() {
        UUID uuidUser = UUID.randomUUID();
        UUID uuidCrapsGame = UUID.randomUUID();
        List<UUID> crapsGameList = List.of(uuidCrapsGame);
        Map<UUID, List<UUID>> userGames = Map.of(uuidUser, crapsGameList);
        CrapsDocument crapsDocument = new CrapsDocument(null, null,
                null, userGames);
        assertEquals(userGames, crapsDocument.getUserGames());
    }

}