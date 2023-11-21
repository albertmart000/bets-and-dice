package com.betsanddice.tutorial.dto;

import com.betsanddice.tutorial.helper.ResourceHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
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
    private GameTutorialDto gameTutorialDtoToSerialize;
    private GameTutorialDto gameTutorialDtoFromDeserialize;

    @BeforeEach
    void setUp() {
        UUID uuidGameTutorial = UUID.fromString("c8a5440d-6466-463a-bccc-7fefbe9396e4");
        String rules = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur...";

        gameTutorialDtoToSerialize = new GameTutorialDto(uuidGameTutorial,"Craps", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur...");
        gameTutorialDtoFromDeserialize = new GameTutorialDto(uuidGameTutorial,"Craps", rules);
    }

    @Test
    @DisplayName("Serialization GameTutorialDto test")
    @SneakyThrows({JsonProcessingException.class})
    void rightSerializationTest() {
        String jsonResult = mapper
                .writer(new DefaultPrettyPrinter().withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE))
                .writeValueAsString(gameTutorialDtoToSerialize);
        String jsonExpected = new ResourceHelper(gameTutorialJsonPath).readResourceAsString().orElse(null);
        assertEquals(jsonExpected, jsonResult);
    }

    @Test
    @DisplayName("Deserialization GameTutorialDto test")
    @SneakyThrows(IOException.class)
    void rightDeserializationTest() {
        String crapsGameJsonSource = new ResourceHelper(gameTutorialJsonPath).readResourceAsString().orElse(null);
        GameTutorialDto dtoResult = mapper.readValue(crapsGameJsonSource, GameTutorialDto.class);
        assertThat(dtoResult).usingRecursiveComparison().isEqualTo(gameTutorialDtoFromDeserialize);
    }
}