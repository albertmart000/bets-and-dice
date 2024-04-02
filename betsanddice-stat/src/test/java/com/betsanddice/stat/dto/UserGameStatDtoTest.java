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

    private final String userStatJsonPath = "json/userStatSerialized.json";
    private UserGameStatDto userGameStatDtoToSerialize;
    private UserGameStatDto userGameStatDtoFromDeserialize;

@BeforeEach
    void setUp(){
    UUID uuidUserStat = UUID.fromString("c73a00ef-bfb1-458a-9c9d-5b1cdfca4a01");
    UUID uuidUser = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");
    UUID uuidGame = UUID.fromString("c8a5440d-6466-463a-bccc-7fefbe9396e4");
    double average = 3.5;

    userGameStatDtoToSerialize = new UserGameStatDto(uuidUserStat, uuidUser, uuidGame, 3.5);
    userGameStatDtoFromDeserialize = new UserGameStatDto(uuidUserStat, uuidUser, uuidGame, average);
}

    @Test
    @DisplayName("Serialization UserStatDto test")
    @SneakyThrows({JsonProcessingException.class})
    void rightSerializationTest() {
        String jsonResult = mapper
                .writer(new DefaultPrettyPrinter().withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE))
                .writeValueAsString(userGameStatDtoToSerialize);
        String jsonExpected = new ResourceHelper(userStatJsonPath).readResourceAsString().orElse(null);
        assertEquals(jsonExpected, jsonResult);
    }

    @Test
    @DisplayName("Deserialization GameTutorialDto test")
    @SneakyThrows(IOException.class)
    void rightDeserializationTest() {
        String crapsGameJsonSource = new ResourceHelper(userStatJsonPath).readResourceAsString().orElse(null);
        UserGameStatDto dtoResult = mapper.readValue(crapsGameJsonSource, UserGameStatDto.class);
        assertThat(dtoResult).usingRecursiveComparison().isEqualTo(userGameStatDtoFromDeserialize);
    }
}