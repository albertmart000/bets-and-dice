package com.betsanddice.game.helper;

import com.betsanddice.game.document.GameDocument;
import com.betsanddice.game.dto.GameDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GameDocumentToDtoConverterTest {

    private DocumentToDtoConverter<GameDocument, GameDto> converter;

    private GameDocument gameDocument1;
    private GameDocument gameDocument2;

    private GameDto gameDto1;
    private GameDto gameDto2;

    @BeforeEach
    public void setUp() {
        converter = new DocumentToDtoConverter<>();

        UUID uuidGame1 = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf3d3");
        UUID uuidGame2 = UUID.fromString("6160a07c-1d0f-4ac0-80b0-ef8f17bcad53");

        UUID uuidTutorial1 = UUID.fromString("990b9699-75cf-42b5-aa0f-cf3a2169c770");
        UUID uuidTutorial2 = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

        UUID uuidStat1 = UUID.fromString("76cb3a63-bc48-4b68-a291-18140a3794d7");
        UUID uuidStat2 = UUID.fromString("795e8c10-d68a-47a0-8f37-79736d6aa632");

        gameDocument1 = new GameDocument(uuidGame1, "Craps", uuidTutorial1, uuidStat1);
        gameDocument2 = new GameDocument(uuidGame2, "SixDice", uuidTutorial2, uuidStat2);

        gameDto1 = new GameDto(uuidGame1, "Craps", uuidTutorial1, uuidStat1);
        gameDto2 = new GameDto(uuidGame2, "SixDice", uuidTutorial2, uuidStat2);
    }

    @Test
    @DisplayName("Conversion from GameDocument to GameDto. Testing 'fromDocumentToDto' method.")
    void testConvertFromDocumentToDto() {
        GameDocument gameDocumentMocked = gameDocument1;
        GameDto resultDto = converter.fromDocumentToDto(gameDocumentMocked, GameDto.class);
        GameDto expectedDto = gameDto1;

        assertThat(expectedDto).usingRecursiveComparison()
                .isEqualTo(resultDto);
    }

    @Test
    @DisplayName("Testing Flux conversion. Test fromDocumentFluxToDtoFlux method.")
    void fromFluxDocToFluxDto() {
        Flux<GameDto> resultDto = converter.fromDocumentFluxToDtoFlux(Flux.just(gameDocument1, gameDocument2), GameDto.class);

        GameDto expectedDto1 = gameDto1;
        GameDto expectedDto2 = gameDto2;

        assertThat(resultDto.count().block()).isEqualTo(Long.valueOf(2));
        assertThat(resultDto.blockFirst()).usingRecursiveComparison()
                .isEqualTo(expectedDto1);
        assertThat(resultDto.blockLast()).usingRecursiveComparison()
                .isEqualTo(expectedDto2);
    }

}