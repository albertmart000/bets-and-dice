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
class UserAllGameStatDtoTest {

    @Autowired
    private ObjectMapper mapper;

    private final String userAllGameStatJsonPath = "json/userAllGameStatSerialized.json";
    private UserAllGameStatDto userAllGameStatDtoToSerialize;
    private UserAllGameStatDto userAllGameStatDtoFromDeserialize;

    @BeforeEach
    void setUp() {

        UUID userAllGameStatUuid = UUID.fromString("59ba606c-aecb-4f5a-ba01-9c39caf88bfa");
        UUID userUuid = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");
        UUID userStatGameUuid1 = UUID.fromString("6dad0dd8-12dd-4073-84d6-10c066642d43");
        UUID userStatGameUuid2 = UUID.fromString("0babfb1c-6c22-4001-8a76-359f73769bee");

        List<UUID> userGameStatDtoList = List.of(userStatGameUuid1, userStatGameUuid2);

        userAllGameStatDtoToSerialize = new UserAllGameStatDto(userAllGameStatUuid, userUuid,
                userGameStatDtoList);
        userAllGameStatDtoFromDeserialize = new UserAllGameStatDto(userAllGameStatUuid, userUuid,
                userGameStatDtoList);
    }

    @Test
    @DisplayName("Serialization UserAllGameStatDto test")
    @SneakyThrows({JsonProcessingException.class})
    void rightSerializationTest() {
        String jsonResult = mapper
                .writer(new DefaultPrettyPrinter().withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE))
                .writeValueAsString(userAllGameStatDtoToSerialize);
        String jsonExpected = new ResourceHelper(userAllGameStatJsonPath).readResourceAsString().orElse(null);
        assertEquals(jsonExpected, jsonResult);
    }

    @Test
    @DisplayName("Deserialization UserAllGameStatDto test")
    @SneakyThrows(IOException.class)
    void rightDeserializationTest() {
        String userAllGameStatJsonSource = new ResourceHelper(userAllGameStatJsonPath).readResourceAsString().orElse(null);
        UserAllGameStatDto dtoResult = mapper.readValue(userAllGameStatJsonSource, UserAllGameStatDto.class);
        assertThat(dtoResult).usingRecursiveComparison().isEqualTo(userAllGameStatDtoFromDeserialize);
    }
}