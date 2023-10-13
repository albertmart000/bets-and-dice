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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserDtoTest {

    @Autowired
    private ObjectMapper mapper;

    private final String userJsonPath = "json/UserSerialized.json";

    private UserDto userDtoToSerialize;

    private UserDto userDtoFromDeserialization;

    @BeforeEach
    void setUp() {
        UUID uuidUser = UUID.fromString("81099a9e-0d59-4571-a04c-31a08a711e3b");

        UUID uuidGame1 = UUID.fromString("dcacb291-b4aa-4029-8e9b-284c8ca80296");
        UUID uuidGame2 = UUID.fromString("09fabe32-7362-4bfb-ac05-b7bf854c6e0f");

        UUID uuidStatistics1 = UUID.fromString("70e9755e-9e83-41d3-853a-665f1f2a2f5c");
        UUID uuidStatistics2 = UUID.fromString("bb7897b8-517d-4843-8c17-e347aba086ca");

        userDtoToSerialize = buildUserWithBasicInfoDto(uuidUser, "Morrow", "Montgomery", "2000-03-03T00:00:00.000+00:00",
                "Player2", "morrowmontgomery@email.com", "player2", "2023-01-31T12:46:29 -01:00", 100,
                buildGamesSorted(uuidGame1, uuidGame2), buildStatisticsSorted(uuidStatistics1, uuidStatistics2));

        userDtoFromDeserialization = buildUserWithBasicInfoDto(uuidUser, "Morrow", "Montgomery", "2000-03-03T00:00:00.000+00:00",
                "Player2", "morrowmontgomery@email.com", "player2", "2023-01-31T12:46:29 -01:00", 100,
                buildGames(uuidGame1, uuidGame2), buildStatistics(uuidStatistics1, uuidStatistics2));
    }

    @Test
    @DisplayName("Serialization UserDto test")
    @SneakyThrows({JsonProcessingException.class})
    void rightSerializationTest() {
        String jsonResult = mapper
                .writer(new DefaultPrettyPrinter().withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE))
                .writeValueAsString(userDtoToSerialize);
        String jsonExpected = new ResourceHelper(userJsonPath).readResourceAsString().orElse(null);
        assertEquals(jsonExpected, jsonResult);
    }

    @Test
    @DisplayName("Deserialization UserDto test")
    @SneakyThrows(IOException.class)
    void rightDeserializationTest() {
        String challengeJsonSource = new ResourceHelper(userJsonPath).readResourceAsString().orElse(null);
        UserDto dtoResult = mapper.readValue(challengeJsonSource, UserDto.class);
        assertThat(dtoResult).usingRecursiveComparison().isEqualTo(userDtoFromDeserialization);
    }

    private UserDto buildUserWithBasicInfoDto
            (UUID user_id, String name, String surname, String birthdate, String nickname, String email,
             String password, String registered, int cash, List<UUID> games, List<UUID> statistics) {
        return UserDto.builder()
                .uuid(user_id)
                .name(name)
                .surname(surname)
                .birthdate(birthdate)
                .nickname(nickname)
                .email(email)
                .password(password)
                .registrationDate(registered)
                .cash(BigDecimal.valueOf(cash))
                .games(games)
                .statistics(statistics)
                .build();
    }

    private List<UUID> buildGamesSorted(UUID uuidGame1, UUID uuidGame2) {
        List<UUID> buildGamesSorted = new ArrayList<>();
        buildGamesSorted.add(uuidGame1);
        buildGamesSorted.add(uuidGame2);
        return buildGamesSorted;
    }

    private List<UUID> buildStatisticsSorted(UUID uuidStatistics1, UUID uuidStatistics2) {
        List<UUID> buildStatisticsSorted = new ArrayList<>();
        buildStatisticsSorted.add(uuidStatistics1);
        buildStatisticsSorted.add(uuidStatistics2);
        return buildStatisticsSorted;
    }

    private List<UUID> buildGames(UUID uuidGame1, UUID uuidGame2) {
        return List.copyOf(List.of(uuidGame1, uuidGame2));
    }

    private List<UUID> buildStatistics(UUID uuidStatistics1, UUID uuidStatistics2) {
        return List.copyOf(List.of(uuidStatistics1, uuidStatistics2));
    }
}