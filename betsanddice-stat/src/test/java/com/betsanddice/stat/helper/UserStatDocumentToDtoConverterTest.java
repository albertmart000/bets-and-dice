package com.betsanddice.stat.helper;

import com.betsanddice.stat.document.UserStatDocument;
import com.betsanddice.stat.dto.UserStatDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserStatDocumentToDtoConverterTest {

    private DocumentToDtoConverter<UserStatDocument, UserStatDto> converter;

    private UserStatDocument userStatDocument1;
    private UserStatDocument userStatDocument2;

    private UserStatDto userStatDto1;
    private UserStatDto userStatDto2;

    @BeforeEach
    void setUp() {
        converter = new DocumentToDtoConverter<>();

        UUID uuidUserStat1 = UUID.fromString("c73a00ef-bfb1-458a-9c9d-5b1cdfca4a01");
        UUID uuidUserStat2 = UUID.fromString("76628e83-b879-4186-8263-3325236a5fa5");

        UUID uuidUser = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

        UUID uuidGame1 = UUID.fromString("c8a5440d-6466-463a-bccc-7fefbe9396e4");
        UUID uuidGame2 = UUID.fromString("9cc65b00-8412-46e7-ba6f-ead17a9fe167");

        double average = 3.5;

        userStatDocument1 = new UserStatDocument(uuidUserStat1, uuidUser, uuidGame1, average);
        userStatDocument2 = new UserStatDocument(uuidUserStat2, uuidUser, uuidGame2, average);

        userStatDto1 = new UserStatDto(uuidUserStat1, uuidUser, uuidGame1, average);
        userStatDto2 = new UserStatDto(uuidUserStat2, uuidUser, uuidGame2, average);
    }

    @Test
    @DisplayName("Conversion from UserStatDocument to UserStatDto. Testing 'fromDocumentToDto' method.")
    void testConvertFromDocumentToDtoTest() {
        UserStatDocument userStatDocumentMocked = userStatDocument1;
        UserStatDto resultDto = converter.fromDocumentToDto(userStatDocumentMocked, UserStatDto.class);
        UserStatDto expectedDto = userStatDto1;

        assertThat(expectedDto).usingRecursiveComparison()
                .isEqualTo(resultDto);
    }

    @Test
    @DisplayName("Testing Flux conversion. Test fromDocumentFluxToDtoFlux method.")
    void fromFluxDocToFluxDtoTest() {
        Flux<UserStatDto> resultDto = converter.fromDocumentFluxToDtoFlux(Flux.just(userStatDocument1, userStatDocument2), UserStatDto.class);

        UserStatDto expectedDto1 = userStatDto1;
        UserStatDto expectedDto2 = userStatDto2;

        assertThat(resultDto.count().block()).isEqualTo(Long.valueOf(2));
        assertThat(resultDto.blockFirst()).usingRecursiveComparison()
                .isEqualTo(expectedDto1);
        assertThat(resultDto.blockLast()).usingRecursiveComparison()
                .isEqualTo(expectedDto2);
    }

}
