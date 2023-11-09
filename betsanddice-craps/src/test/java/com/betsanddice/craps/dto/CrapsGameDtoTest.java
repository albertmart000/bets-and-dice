package com.betsanddice.craps.dto;

import com.betsanddice.craps.helper.ResourceHelper;
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
class CrapsGameDtoTest {

    @Autowired
    private ObjectMapper mapper;

    private final String crapsGameJsonPath = "json/crapsGameSerialized.json";
    private CrapsGameDto crapsGameDtoToSerialize;
    private CrapsGameDto crapsGameDtoFromDeserialize;

    @BeforeEach
    void setUp() {
        UUID uuidCrapsGame = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf3d3");

        UUID uuidUser = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");
        int[][] diceRolls = new int[][]{{1, 2}, {4, 5}, {3, 4}};

        crapsGameDtoToSerialize = buildBasicCrapsGameDto (uuidCrapsGame, uuidUser, "Player1",
                "2023-01-31T12:46:29 -01:00", diceRolls);

        crapsGameDtoFromDeserialize = buildBasicCrapsGameDto (uuidCrapsGame, uuidUser, "Player1",
                "2023-01-31T12:46:29 -01:00", diceRolls);
    }

    @Test
    @DisplayName("Serialization CrapsGameDto test")
    @SneakyThrows({JsonProcessingException.class})
    void rightSerializationTest() {
        String jsonResult = mapper
                .writer(new DefaultPrettyPrinter().withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE))
                .writeValueAsString(crapsGameDtoToSerialize);
        String jsonExpected = new ResourceHelper(crapsGameJsonPath).readResourceAsString().orElse(null);
        assertEquals(jsonExpected, jsonResult);
    }

    @Test
    @DisplayName("Deserialization CrapsGameDto test")
    @SneakyThrows(IOException.class)
    void rightDeserializationTest() {
        String crapsGameJsonSource = new ResourceHelper(crapsGameJsonPath).readResourceAsString().orElse(null);
        CrapsGameDto dtoResult = mapper.readValue(crapsGameJsonSource, CrapsGameDto.class);
        assertThat(dtoResult).usingRecursiveComparison().isEqualTo(crapsGameDtoFromDeserialize);
    }

    private CrapsGameDto buildBasicCrapsGameDto(UUID uuidCrapsGame, UUID uuidUser, String userNickname,
                                                String date, int[][] diceRolls) {
        return CrapsGameDto.builder()
                .uuid(uuidCrapsGame)
                .uuid(uuidUser)
                .userNickname(userNickname)
                .date(date)
                .diceRolls(diceRolls)
                .build();
    }

}