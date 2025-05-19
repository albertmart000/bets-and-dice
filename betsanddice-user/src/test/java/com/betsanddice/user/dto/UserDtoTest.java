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
class UserDtoTest {

    @Autowired
    private ObjectMapper mapper;

    private final String userJsonPath = "json/userSerialized.json";
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.fromString("81099a9e-0d59-4571-a04c-31a08a711e3b");
        userDto = new UserDto(userId, "Morrow", "Montgomery", "Player1", "morrowmontgomery@email.com",
                "player1", "2000-03-03T00:00:00.000+00:00", "2020-03-03T00:00:00.000+00:00", "PLAYER");
    }

    @Test
    @DisplayName("Serialization UserDto test")
    @SneakyThrows({JsonProcessingException.class})
    void rightSerializationTest() {
        String jsonResult = mapper
                .writer(new DefaultPrettyPrinter().withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE))
                .writeValueAsString(userDto);
        String jsonExpected = new ResourceHelper(userJsonPath).readResourceAsString().orElse(null);
        assertEquals(jsonExpected, jsonResult);
    }

    @Test
    @DisplayName("Deserialization UserDto test")
    @SneakyThrows(IOException.class)
    void rightDeserializationTest() {
        String challengeJsonSource = new ResourceHelper(userJsonPath).readResourceAsString().orElse(null);
        UserDto dtoResult = mapper.readValue(challengeJsonSource, UserDto.class);
        assertThat(dtoResult).usingRecursiveComparison().isEqualTo(userDto);
    }

}