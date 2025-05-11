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
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRegisterDtoTest {

    @Autowired
    private ObjectMapper mapper;

    private final String userRegisterJsonPath = "json/userRegisterSerialized.json";
    private UserRegisterDto userRegisterDto;

    @BeforeEach
    void setUp() {
        userRegisterDto = new UserRegisterDto("Morrow", "Montgomery", "Player1", "morrowmontgomery@email.com",
                "player1", LocalDate.parse("2000-03-03"));
    }

    @Test
    @DisplayName("Serialization UserRegisterDto test")
    @SneakyThrows({JsonProcessingException.class})
    void rightSerializationTest() {
        String jsonResult = mapper
                .writer(new DefaultPrettyPrinter().withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE))
                .writeValueAsString(userRegisterDto);
        String jsonExpected = new ResourceHelper(userRegisterJsonPath).readResourceAsString().orElse(null);
        assertEquals(jsonExpected, jsonResult);
    }

    @Test
    @DisplayName("Deserialization UserRegisterDto test")
    @SneakyThrows(IOException.class)
    void rightDeserializationTest() {
        String challengeJsonSource = new ResourceHelper(userRegisterJsonPath).readResourceAsString().orElse(null);
        UserRegisterDto dtoResult = mapper.readValue(challengeJsonSource, UserRegisterDto.class);
        assertThat(dtoResult).usingRecursiveComparison().isEqualTo(userRegisterDto);
    }

}