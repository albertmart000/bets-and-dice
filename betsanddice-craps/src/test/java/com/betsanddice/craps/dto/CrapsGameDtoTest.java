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
import java.util.List;
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

        UUID uuidUser= UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

        UUID uuid1 = UUID.fromString("c341527c-6379-4d8a-a885-c938b121fb75");
        UUID uuid2 = UUID.fromString("5c12481c-e571-4808-a7dc-a9247e5c1037");
        List<UUID> diceRollsList = List.of(uuid1, uuid2);

        crapsGameDtoToSerialize = new CrapsGameDto(uuidCrapsGame, uuidUser, "Player1",
                "2023-01-31T12:46:29 -01:00", diceRollsList);
        crapsGameDtoFromDeserialize = new CrapsGameDto(uuidCrapsGame, uuidUser, "Player1",
                "2023-01-31T12:46:29 -01:00", diceRollsList);

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

}