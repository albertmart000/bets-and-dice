package com.betsanddice.tutorial.document;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GameTutorialDocumentTest {
    @Test
    void getUuidTest() {
        UUID uuid = UUID.randomUUID();
        GameTutorialDocument gameTutorialDocument = new GameTutorialDocument(uuid, null, null,
                null);
        assertEquals(uuid, gameTutorialDocument.getUuid());
    }

    @Test
    void getGameIdTest() {
        UUID uuid = UUID.randomUUID();
        GameTutorialDocument gameTutorialDocument = new GameTutorialDocument(null, uuid, null,
                null);

        assertEquals(uuid, gameTutorialDocument.getGameId());
    }

    @Test
    void getGameNameTest() {
        String gameName = "gameName";
        GameTutorialDocument gameTutorialDocument = new GameTutorialDocument(null, null, gameName,
                null);
        assertEquals(gameName, gameTutorialDocument.getGameName());
    }

    @Test
    void getRulesTest() {
        String rules = "rules";
        GameTutorialDocument gameTutorialDocument = new GameTutorialDocument(null, null, null,
                rules);
        assertEquals(rules, gameTutorialDocument.getRules());
    }

}