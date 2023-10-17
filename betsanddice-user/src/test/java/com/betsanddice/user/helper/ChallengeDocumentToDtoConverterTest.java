package com.betsanddice.user.helper;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChallengeDocumentToDtoConverterTest {

    private UserDocumentToDtoConverter converter;

    private UserDocument userDocument1;
    private UserDocument userDocument2;

    private UserDto userDto1;
    private UserDto userDto2;

    @BeforeEach
    public void setUp() {
        converter = new UserDocumentToDtoConverter();

        UUID uuidUser1 = UUID.fromString("81099a9e-0d59-4571-a04c-31a08a711e3b");
        UUID uuidUser2 = UUID.fromString("26977eee-89f8-11ec-a8a3-0242ac120003");

        UUID uuidGame1 = UUID.fromString("dcacb291-b4aa-4029-8e9b-284c8ca80296");
        UUID uuidGame2 = UUID.fromString("09fabe32-7362-4bfb-ac05-b7bf854c6e0f");
        List<UUID> gameList = List.of(uuidGame1, uuidGame2);

        UUID uuidStatistics1 = UUID.fromString("70e9755e-9e83-41d3-853a-665f1f2a2f5c");
        UUID uuidStatistics2 = UUID.fromString("bb7897b8-517d-4843-8c17-e347aba086ca");
        List<UUID> statisticsList = List.of(uuidStatistics1, uuidStatistics2);

        LocalDate birthdate = LocalDate.of(2000, 3, 3);
        LocalDateTime registered = LocalDateTime.of(2023, 1, 31, 12, 0, 0);

        userDocument1 = new UserDocument(uuidUser1, "Morrow", "Montgomery", birthdate,
                "Player2", "morrowmontgomery@email.com", "player2", registered,
                "level", BigDecimal.valueOf(100), gameList, statisticsList);

        userDocument2 = new UserDocument(uuidUser2, "Morrow", "Montgomery", birthdate,
                "Player2", "morrowmontgomery@email.com", "player2", registered,
                "level", BigDecimal.valueOf(100), gameList, statisticsList);

        userDto1 = getUserDtoMocked(uuidUser1, "Morrow", "Montgomery", "2000-03-03", "Player2",
                "morrowmontgomery@email.com", "player2", "2023-01-31", "level",
                100, gameList, statisticsList);

        userDto2 = getUserDtoMocked(uuidUser2, "Morrow", "Montgomery", "2000-03-03", "Player2",
                "morrowmontgomery@email.com", "player2", "2023-01-31", "level",
                100, gameList, statisticsList);
    }

    @Test
    @DisplayName("Conversion from UserDocument to UserDto. Testing 'convertDocumentToDto' method.")
    void testConvertToDto() {
        UserDocument userDocumentMocked = userDocument1;
        UserDto resultDto = converter.convertDocumentToDto(userDocumentMocked);
        UserDto expectedDto = userDto1;

        assertThat(expectedDto).usingRecursiveComparison()
                .isEqualTo(resultDto);
    }

    @Test
    @DisplayName("Testing Flux conversion. Test convertDocumentFluxToDtoFlux method.")
    void fromFluxDocToFluxDto() {
        UserDocument userDocument1 = this.userDocument1;
        UserDocument userDocument2 = this.userDocument2;

        Flux<UserDto> resultDto = converter.convertDocumentFluxToDtoFlux(Flux.just(userDocument1, userDocument2));

        UserDto expectedDto1 = userDto1;
        UserDto expectedDto2 = userDto2;

        assertThat(resultDto.count().block()).isEqualTo(Long.valueOf(2));
        assertThat(resultDto.blockFirst()).usingRecursiveComparison()
                .isEqualTo(expectedDto1);
        assertThat(resultDto.blockLast()).usingRecursiveComparison()
                .isEqualTo(expectedDto2);
    }

    private UserDto getUserDtoMocked(UUID user_id, String name, String surname, String birthdate, String nickname,
                                     String email, String password, String registered, String level, int cash,
                                     List<UUID> games, List<UUID> statistics) {
        UserDto userDtoMocked = mock(UserDto.class);
        when(userDtoMocked.getUuid()).thenReturn(user_id);
        when(userDtoMocked.getName()).thenReturn(name);
        when(userDtoMocked.getSurname()).thenReturn(surname);
        when(userDtoMocked.getBirthdate()).thenReturn(birthdate);
        when(userDtoMocked.getNickname()).thenReturn(nickname);
        when(userDtoMocked.getEmail()).thenReturn(email);
        when(userDtoMocked.getPassword()).thenReturn(password);
        when(userDtoMocked.getRegistrationDate()).thenReturn(registered);
        when(userDtoMocked.getLevel()).thenReturn(level);
        when(userDtoMocked.getCash()).thenReturn(BigDecimal.valueOf(cash));
        when(userDtoMocked.getGames()).thenReturn(games);
        when(userDtoMocked.getStatistics()).thenReturn(statistics);
        return userDtoMocked;
    }

}

