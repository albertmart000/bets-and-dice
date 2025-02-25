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
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ResultDtoTest {

    @Autowired
    private ObjectMapper mapper;

    private final String resultJsonPath = "json/resultSerialized.json";
    private ResultDto resultBetDto;

    @BeforeEach
    void setUp() {
        resultBetDto = new ResultDto(5, true, 2.0, 20.0);
    }

    @Test
    @DisplayName("Serialization ResultDto test")
    @SneakyThrows({JsonProcessingException.class})
    void rightSerializationTest() {
        String jsonResult = mapper
                .writer(new DefaultPrettyPrinter().withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE))
                .writeValueAsString(resultBetDto);
        String jsonExpected = new ResourceHelper(resultJsonPath).readResourceAsString().orElse(null);
        assertEquals(jsonExpected, jsonResult);
    }

    @Test
    @DisplayName("Deserialization ResultDto test")
    @SneakyThrows(IOException.class)
    void rightDeserializationTest() {
        String resultJsonSource = new ResourceHelper(resultJsonPath).readResourceAsString().orElse(null);
        ResultDto dtoResult = mapper.readValue(resultJsonSource, ResultDto.class);
        assertThat(dtoResult).usingRecursiveComparison().isEqualTo(resultBetDto);
    }

}