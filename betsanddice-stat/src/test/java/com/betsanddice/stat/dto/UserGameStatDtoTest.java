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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserGameStatDtoTest {

    @Autowired
    private ObjectMapper mapper;

    private final String userGameStatJsonPath = "json/userGameStatSerialized.json";
    private UserGameStatDto userGameStatDtoToSerialize;
    private UserGameStatDto userGameStatDtoFromDeserialize;

@BeforeEach
    void setUp(){
    UUID userGameStatUuid = UUID.fromString("c73a00ef-bfb1-458a-9c9d-5b1cdfca4a01");
    UUID userUuid = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");
    UUID gameUuid = UUID.fromString("c8a5440d-6466-463a-bccc-7fefbe9396e4");
    String gameName = "Craps";
    int gamesPlayed = 50;
    int gamesWonOrAttempts = 25;
    double average = 0.5;
    int ranking = 1;

    userGameStatDtoToSerialize = new UserGameStatDto(userGameStatUuid, userUuid, gameUuid, gameName,
            50, 25, 0.5, 1);
    userGameStatDtoFromDeserialize = new UserGameStatDto(userGameStatUuid, userUuid, gameUuid, gameName,
            gamesPlayed, gamesWonOrAttempts, average, ranking);
}

    @Test
    @DisplayName("Serialization UserGameStatDto test")
    @SneakyThrows({JsonProcessingException.class})
    void rightSerializationTest() {
        String jsonResult = mapper
                .writer(new DefaultPrettyPrinter().withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE))
                .writeValueAsString(userGameStatDtoToSerialize);
        String jsonExpected = new ResourceHelper(userGameStatJsonPath).readResourceAsString().orElse(null);
        assertEquals(jsonExpected, jsonResult);
    }

    @Test
    @DisplayName("Deserialization UserGameStatDto test")
    @SneakyThrows(IOException.class)
    void rightDeserializationTest() {
        String crapsGameJsonSource = new ResourceHelper(userGameStatJsonPath).readResourceAsString().orElse(null);
        UserGameStatDto dtoResult = mapper.readValue(crapsGameJsonSource, UserGameStatDto.class);
        assertThat(dtoResult).usingRecursiveComparison().isEqualTo(userGameStatDtoFromDeserialize);
    }
}