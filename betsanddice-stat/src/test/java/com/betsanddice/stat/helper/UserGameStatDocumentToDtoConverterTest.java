package com.betsanddice.stat.helper;

import com.betsanddice.stat.document.UserGameStatDocument;
import com.betsanddice.stat.dto.UserGameStatDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class UserGameStatDocumentToDtoConverterTest {

    private DocumentToDtoConverter<UserGameStatDocument, UserGameStatDto> converter;

    private UserGameStatDocument userGameStatDocument1;
    private UserGameStatDocument userGameStatDocument2;

    private UserGameStatDto userGameStatDto1;
    private UserGameStatDto userGameStatDto2;

    @BeforeEach
    void setUp() {
        converter = new DocumentToDtoConverter<>();

        UUID uuidUserStat1 = UUID.fromString("c73a00ef-bfb1-458a-9c9d-5b1cdfca4a01");
        UUID uuidUserStat2 = UUID.fromString("76628e83-b879-4186-8263-3325236a5fa5");

        UUID uuidUser = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

        UUID uuidGame1 = UUID.fromString("c8a5440d-6466-463a-bccc-7fefbe9396e4");
        UUID uuidGame2 = UUID.fromString("9cc65b00-8412-46e7-ba6f-ead17a9fe167");

        double average = 3.5;

        userGameStatDocument1 = new UserGameStatDocument(uuidUserStat1, uuidUser, uuidGame1, average);
        userGameStatDocument2 = new UserGameStatDocument(uuidUserStat2, uuidUser, uuidGame2, average);

        userGameStatDto1 = new UserGameStatDto(uuidUserStat1, uuidUser, uuidGame1, average);
        userGameStatDto2 = new UserGameStatDto(uuidUserStat2, uuidUser, uuidGame2, average);
    }

    @Test
    @DisplayName("Conversion from UserGameStatDocument to UserGameStatDto. Testing 'fromDocumentToDto' method.")
    void testConvertFromDocumentToDtoTest() {
        UserGameStatDocument userGameStatDocumentMocked = userGameStatDocument1;
        UserGameStatDto resultDto = converter.fromDocumentToDto(userGameStatDocumentMocked, UserGameStatDto.class);
        UserGameStatDto expectedDto = userGameStatDto1;

        assertThat(expectedDto).usingRecursiveComparison()
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
                .isEqualTo(expectedDto1);
        assertThat(resultDto.blockLast()).usingRecursiveComparison()
                .isEqualTo(expectedDto2);
    }

}
