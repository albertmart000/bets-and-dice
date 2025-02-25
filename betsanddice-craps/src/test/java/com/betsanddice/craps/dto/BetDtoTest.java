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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BetDtoTest {

    @Autowired
    private ObjectMapper mapper;

    private final String betJsonPath = "json/betSerialized.json";
    private BetDto betDto;

    @BeforeEach
    void setUp() {
        betDto = new BetDto(7, 5, 10.0);
    }

    @Test
    @DisplayName("Serialization BetDto test")
    @SneakyThrows({JsonProcessingException.class})
    void rightSerializationTest() {
        String jsonResult = mapper
                .writer(new DefaultPrettyPrinter().withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE))
                .writeValueAsString(betDto);
        String jsonExpected = new ResourceHelper(betJsonPath).readResourceAsString().orElse(null);
        assertEquals(jsonExpected, jsonResult);
    }

    @Test
    @DisplayName("Deserialization BettDto test")
    @SneakyThrows(IOException.class)
    void rightDeserializationTest() {
        String resultJsonSource = new ResourceHelper(betJsonPath).readResourceAsString().orElse(null);
        BetDto dtoResult = mapper.readValue(resultJsonSource, BetDto.class);
        assertThat(dtoResult).usingRecursiveComparison().isEqualTo(betDto);
    }

}