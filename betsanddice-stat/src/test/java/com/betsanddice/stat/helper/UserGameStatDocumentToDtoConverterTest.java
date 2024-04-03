package com.betsanddice.stat.helper;

import com.betsanddice.stat.document.UserGameStatDocument;
import com.betsanddice.stat.dto.UserGameStatDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserGameStatDocumentToDtoConverterTest {

    private DocumentToDtoConverter<UserGameStatDocument, UserGameStatDto> converter;

    private UserGameStatDocument userGameStatDocument1;
    private UserGameStatDocument userGameStatDocument2;

    private UserGameStatDto userGameStatDto1;
    private UserGameStatDto userGameStatDto2;

    @BeforeEach
    void setUp() {
        converter = new DocumentToDtoConverter<>();

        UUID userGameStatUuid1 = UUID.fromString("c73a00ef-bfb1-458a-9c9d-5b1cdfca4a01");
        UUID userGameStatUuid2 = UUID.fromString("76628e83-b879-4186-8263-3325236a5fa5");

        UUID userUuid = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

        UUID gameUuid1 = UUID.fromString("c8a5440d-6466-463a-bccc-7fefbe9396e4");
        UUID gameUuid2 = UUID.fromString("9cc65b00-8412-46e7-ba6f-ead17a9fe167");

        String gameName = "game";
        int gamesPlayed = 50;
        int gamesWonOrAttempts = 0;

        double average = 0.5;
        int ranking = 1;

        userGameStatDocument1 = new UserGameStatDocument(userGameStatUuid1, userUuid, gameUuid1, gameName,
                gamesPlayed, gamesWonOrAttempts);
        userGameStatDocument2 = new UserGameStatDocument(userGameStatUuid2, userUuid, gameUuid2, gameName,
                gamesPlayed, gamesWonOrAttempts);

        userGameStatDto1 = new UserGameStatDto(userGameStatUuid1, userUuid, gameUuid1, gameName, gamesPlayed,
                gamesWonOrAttempts, average, ranking);
        userGameStatDto2 = new UserGameStatDto(userGameStatUuid2, userUuid, gameUuid2, gameName, gamesPlayed,
                gamesWonOrAttempts, average, ranking);
    }

    @Test
    @DisplayName("Conversion from UserGameStatDocument to UserGameStatDto. Testing 'fromDocumentToDto' method.")
    void testConvertFromDocumentToDtoTest() {
        UserGameStatDocument userGameStatDocumentMocked = userGameStatDocument1;
        UserGameStatDto resultDto = converter.fromDocumentToDto(userGameStatDocumentMocked, UserGameStatDto.class);
        UserGameStatDto expectedDto = userGameStatDto1;

        assertThat(expectedDto).usingRecursiveComparison()
                .ignoringFields("average", "ranking")
                .isEqualTo(resultDto);
    }

    @Test
    @DisplayName("Testing Flux conversion. Test fromDocumentFluxToDtoFlux method.")
    void fromFluxDocToFluxDtoTest() {
        Flux<UserGameStatDto> resultDto = converter.fromDocumentFluxToDtoFlux(Flux.just(userGameStatDocument1, userGameStatDocument2), UserGameStatDto.class);

        UserGameStatDto expectedDto1 = userGameStatDto1;
        UserGameStatDto expectedDto2 = userGameStatDto2;

        assertThat(resultDto.count().block()).isEqualTo(Long.valueOf(2));
        assertThat(resultDto.blockFirst()).usingRecursiveComparison()
                .ignoringFields("average", "ranking")
                .isEqualTo(expectedDto1);
        assertThat(resultDto.blockLast()).usingRecursiveComparison()
                .ignoringFields("average", "ranking")
                .isEqualTo(expectedDto2);
    }

}
