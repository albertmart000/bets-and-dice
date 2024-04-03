package com.betsanddice.stat.dto;

import com.betsanddice.stat.helper.ResourceHelper;
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
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GameStatDtoTest {

    @Autowired
    private ObjectMapper mapper;

    private final String gameStatJsonPath = "json/gameStatSerialized.json";
    private GameStatDto gameStatDtoToSerialize;
    private GameStatDto gameStatDtoFromDeserialize;

    @BeforeEach
    void setUp() {
        UUID gameStatUuid = UUID.fromString("de590034-81b1-40ae-a2ab-5bee2091b91a");
        UUID gameUuid = UUID.fromString("c8a5440d-6466-463a-bccc-7fefbe9396e4");

        String gameName = "Craps";
        int gamesPlayed = 500;

        UUID userUuid1 = UUID.fromString("7a2bd501-3ec9-43fe-b59b-8caee200b5cf");
        UUID userUuid2 = UUID.fromString("08f3442d-07cc-4a4a-a329-9a222b8a820f");

        List<UUID> usersRanking = List.of(userUuid1, userUuid2);

        gameStatDtoToSerialize = new GameStatDto(gameStatUuid, gameUuid, gameName, gamesPlayed, usersRanking);
        gameStatDtoFromDeserialize = new GameStatDto(gameStatUuid, gameUuid, gameName, gamesPlayed, usersRanking);
    }

    @Test
    @DisplayName("Serialization GameStatDto test")
    @SneakyThrows({JsonProcessingException.class})
    void rightSerializationTest() {
        String jsonResult = mapper
                .writer(new DefaultPrettyPrinter().withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE))
                .writeValueAsString(gameStatDtoToSerialize);
        String jsonExpected = new ResourceHelper(gameStatJsonPath).readResourceAsString().orElse(null);
        assertEquals(jsonExpected, jsonResult);
    }

    @Test
    @DisplayName("Deserialization GameStatDto test")
    @SneakyThrows(IOException.class)
    void rightDeserializationTest() {
        String gameStatJsonSource = new ResourceHelper(gameStatJsonPath).readResourceAsString().orElse(null);
        GameStatDto dtoResult = mapper.readValue(gameStatJsonSource, GameStatDto.class);
        assertThat(dtoResult).usingRecursiveComparison().isEqualTo(gameStatDtoFromDeserialize);
    }
}