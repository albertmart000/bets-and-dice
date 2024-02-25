package com.betsanddice.game.dto;

import com.betsanddice.game.helper.ResourceHelper;
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
class GameDtoTest {

    @Autowired
    private ObjectMapper mapper;

    private final String gameJsonPath = "json/gameSerialized.json";

    private GameDto gameDto;

    @BeforeEach
    void setUp() {
        UUID uuidGame = UUID.fromString("c8a5440d-6466-463a-bccc-7fefbe9396e4");
        UUID uuidTutorialDocument = UUID.fromString("990b9699-75cf-42b5-aa0f-cf3a2169c770");
        UUID uuidStatDocument = UUID.fromString("9555e938-762a-4c0e-ae30-859cf47c59c6");

        gameDto = buildGameDto(uuidGame, "Craps", uuidTutorialDocument, uuidStatDocument);
    }

    @Test
    @DisplayName("Serialization GameDto test")
    @SneakyThrows({JsonProcessingException.class})
    void rightSerializationTest() {
        GameDto dtoSerializable = gameDto;
        String jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dtoSerializable);
        String jsonExpected = new ResourceHelper(gameJsonPath).readResourceAsString().orElse(null);
        assertEquals(jsonExpected,jsonResult);
    }

    @Test
    @DisplayName("Deserialization GameDto test")
    @SneakyThrows(IOException.class)
    void rightDeserializationTest() {
        String jsonDeserializable = new ResourceHelper(gameJsonPath).readResourceAsString().orElse(null);
        GameDto dtoResult = mapper.readValue(jsonDeserializable, GameDto.class);
        GameDto dtoExpected = gameDto;
        assertThat(dtoResult).usingRecursiveComparison().isEqualTo(dtoExpected);
    }

    static GameDto buildGameDto(UUID uuidGame, String gameName, UUID uuidTutorialDocument, UUID uuidStatDocument){
        return new GameDto(uuidGame, "Craps", uuidTutorialDocument, uuidStatDocument);
    }
}