package com.betsanddice.user.helper;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserDocumentToDtoConverterTest {

    private DocumentToDtoConverter<UserDocument, UserDto> converter;

    private UserDocument userDocument1;
    private UserDocument userDocument2;

    private UserDto userDto1;
    private UserDto userDto2;


    @BeforeEach
    public void setUp() {
        converter = new DocumentToDtoConverter();

        UUID userId1 = UUID.fromString("81099a9e-0d59-4571-a04c-31a08a711e3b");
        UUID userId2 = UUID.fromString("26977eee-89f8-11ec-a8a3-0242ac120003");

        LocalDate birthdate = LocalDate.of(2000, 3, 3);
        LocalDateTime registered = LocalDateTime.of(2023, 1, 31, 12, 0, 0);

        userDocument1 = new UserDocument(userId1, "Morrow", "Montgomery", birthdate,
                "Player1", "user1@email.com", "player1", registered);

        userDocument2 = new UserDocument(userId2, "Morrow", "Montgomery", birthdate,
                "Player2", "user2@email.com", "player2", registered);

        userDto1 = new UserDto(userId1, "Morrow", "Montgomery", "2000-03-03", "Player1",
                "user1@email.com", "player1");

        userDto2 = new UserDto(userId2, "Morrow", "Montgomery", "2000-03-03", "Player2",
                "user2@email.com", "player2");
    }

    @Test
    @DisplayName("Conversion from UserDocument to UserDto. Testing 'convertDocumentToDto' method.")
    void testConvertToDto() {
        UserDocument userDocumentMocked = userDocument1;
        UserDto resultDto= converter.fromDocumentToDto(userDocumentMocked, UserDto.class);
        UserDto expectedDto = userDto1;

        assertThat(expectedDto).usingRecursiveComparison()
                .isEqualTo(resultDto);
    }

    @Test
    @DisplayName("Testing Flux conversion. Test convertDocumentFluxToDtoFlux method.")
    void fromFluxDocToFluxDto() {
        UserDocument userDocument1 = this.userDocument1;
        UserDocument userDocument2 = this.userDocument2;

        Flux<UserDto> resultDto = converter.fromDocumentFluxToDtoFlux(Flux.just(userDocument1, userDocument2), UserDto.class);

        UserDto expectedDto1 = userDto1;
        UserDto expectedDto2 = userDto2;

        assertThat(resultDto.count().block()).isEqualTo(Long.valueOf(2));
        assertThat(resultDto.blockFirst()).usingRecursiveComparison()
                .isEqualTo(expectedDto1);
        assertThat(resultDto.blockLast()).usingRecursiveComparison()
                .isEqualTo(expectedDto2);
    }

}

