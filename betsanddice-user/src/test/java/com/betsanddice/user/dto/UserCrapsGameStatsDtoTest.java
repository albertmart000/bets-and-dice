package com.betsanddice.user.dto;

import com.betsanddice.user.helper.ResourceHelper;
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
class UserCrapsGameStatsDtoTest {

    @Autowired
    private ObjectMapper mapper;

    private final String userCrapsGameStatsJsonPath = "json/userCrapsGameStatsSerialized.json";
    private UserCrapsGameStatsDto userCrapsGameStatsDto;

    @BeforeEach
    void setUp() {

        UUID uuidUser = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");
        String nameGame = "Craps";
        int gamesPlayed = 2;
        int gamesWon = 1;
        double percentGamesWon = 50.0;
        double totalAmountBet = 20.0;
        double profitObtained = 1.5;

        userCrapsGameStatsDto = new UserCrapsGameStatsDto(uuidUser, nameGame, gamesPlayed, gamesWon,
                percentGamesWon, totalAmountBet, profitObtained);
    }

    @Test
    @DisplayName("Serialization UserCrapsGameStatDto test")
    @SneakyThrows({JsonProcessingException.class})
    void rightSerializationTest() {
        String jsonResult = mapper
                .writer(new DefaultPrettyPrinter().withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE))
                .writeValueAsString(userCrapsGameStatsDto);
        String jsonExpected = new ResourceHelper(userCrapsGameStatsJsonPath).readResourceAsString().orElse(null);
        assertEquals(jsonExpected, jsonResult);
    }

    @Test
    @DisplayName("Deserialization UserCrapsGameStatsDto test")
    @SneakyThrows(IOException.class)
    void rightDeserializationTest() {
        String userCrapsGameStatsDtoJsonSource = new ResourceHelper(userCrapsGameStatsJsonPath).readResourceAsString().orElse(null);
        UserCrapsGameStatsDto dtoResult = mapper.readValue(userCrapsGameStatsDtoJsonSource, UserCrapsGameStatsDto.class);
        assertThat(dtoResult).usingRecursiveComparison().isEqualTo(userCrapsGameStatsDto);
    }
}