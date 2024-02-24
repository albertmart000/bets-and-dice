package com.betsanddice.tutorial.dto;

import com.betsanddice.tutorial.helper.ResourceHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GameTutorialDtoTest {

    @Autowired
    private ObjectMapper mapper;

    private final String gameTutorialJsonPath = "json/gameTutorialSerialized.json";

    private GameTutorialDto gameTutorialDto;

    @BeforeEach
    void setUp() {
        UUID uuidGameTutorial = UUID.fromString("c8a5440d-6466-463a-bccc-7fefbe9396e4");
        UUID uuidGameDocument = UUID.fromString("7f9dcc63-6daf-4ba2-b3c7-e0b59534f856");

        gameTutorialDto = buildGameTutorialDto(uuidGameTutorial, uuidGameDocument, "Craps", "rules");
    }

    @Test
    @DisplayName("Serialization GameTutorialDto test")
    @SneakyThrows({JsonProcessingException.class})
    void rightSerializationTest() {
        GameTutorialDto dtoSerializable = gameTutorialDto;
        String jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dtoSerializable);
        String jsonExpected = new ResourceHelper(gameTutorialJsonPath).readResourceAsString().orElse(null);
        assertEquals(jsonExpected,jsonResult);
    }

    @Test
    @DisplayName("Deserialization GameTutorialDto test")
    @SneakyThrows(IOException.class)
    void rightDeserializationTest() {
        String jsonDeserializable = new ResourceHelper(gameTutorialJsonPath).readResourceAsString().orElse(null);
        GameTutorialDto dtoResult = mapper.readValue(jsonDeserializable, GameTutorialDto.class);
        GameTutorialDto dtoExpected = gameTutorialDto;
        assertThat(dtoResult).usingRecursiveComparison().isEqualTo(dtoExpected);
    }

    static GameTutorialDto buildGameTutorialDto(UUID uuidGameTutorial, UUID uuidGameDocument, String gameName, String rules){
        return new GameTutorialDto(uuidGameTutorial, uuidGameDocument, gameName, rules);
    }

}