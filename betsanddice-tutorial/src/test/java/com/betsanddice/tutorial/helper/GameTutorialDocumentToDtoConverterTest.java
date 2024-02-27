package com.betsanddice.tutorial.helper;

import com.betsanddice.tutorial.document.GameTutorialDocument;
import com.betsanddice.tutorial.dto.GameTutorialDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameTutorialDocumentToDtoConverterTest {

    private GameTutorialDocumentToDtoConverter converter;

    private GameTutorialDocument gameTutorialDocument1;
    private GameTutorialDocument gameTutorialDocument2;

    private GameTutorialDto gameTutorialDto1;
    private GameTutorialDto gameTutorialDto2;

    @BeforeEach
    public void setup() {
        converter = new GameTutorialDocumentToDtoConverter();

        UUID uuidGameTutorial1 = UUID.fromString("c8a5440d-6466-463a-bccc-7fefbe9396e4");
        UUID uuidGameTutorial2 = UUID.fromString("9cc65b00-8412-46e7-ba6f-ead17a9fe167");

        UUID uuidGameDocument1 = UUID.fromString("7f9dcc63-6daf-4ba2-b3c7-e0b59534f856");
        UUID uuidGameDocument2 = UUID.fromString("bf71596f-0dff-4ce7-b6d6-e348fbf914ed");

        gameTutorialDocument1 = new GameTutorialDocument(uuidGameTutorial1, uuidGameDocument1, "Craps", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur...");
        gameTutorialDocument2 = new GameTutorialDocument(uuidGameTutorial2, uuidGameDocument2, "SixDice", "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt...");

        gameTutorialDto1 = getGameTutorialDtoMocked(uuidGameTutorial1, uuidGameDocument1,"Craps", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur...");
        gameTutorialDto2 = getGameTutorialDtoMocked(uuidGameTutorial2, uuidGameDocument2,"SixDice", "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt...");

    }

    @Test
    @DisplayName("Conversion from GameTutorialDocument to GameTutorialDto. Testing 'fromDocumentToDto' method.")
    void testConvertFromDocumentToDto() {
        GameTutorialDocument gameTutorialDocumentMocked = gameTutorialDocument1;
        GameTutorialDto resultDto = converter.fromDocumentToDto(gameTutorialDocumentMocked);
        GameTutorialDto expectedDto = gameTutorialDto1;

        assertThat(expectedDto).usingRecursiveComparison()
                .isEqualTo(resultDto);
    }

    @Test
    @DisplayName("Testing Flux conversion. Test fromDocumentFluxToDtoFlux method.")
    void fromFluxDocToFluxDto() {
        GameTutorialDocument gameTutorialDocument1 = this.gameTutorialDocument1;
        GameTutorialDocument gameTutorialDocument2 = this.gameTutorialDocument2;

        Flux<GameTutorialDto> resultDto = converter.fromDocumentFluxToDtoFlux(Flux.just(gameTutorialDocument1, gameTutorialDocument2));

        GameTutorialDto expectedDto1 = gameTutorialDto1;
        GameTutorialDto expectedDto2 = gameTutorialDto2;

        assertThat(resultDto.count().block()).isEqualTo(Long.valueOf(2));
        assertThat(resultDto.blockFirst()).usingRecursiveComparison()
                .isEqualTo(expectedDto1);
        assertThat(resultDto.blockLast()).usingRecursiveComparison()
                .isEqualTo(expectedDto2);
    }

    private GameTutorialDto getGameTutorialDtoMocked(UUID uuidGameTutorial, UUID uuidGameDocument, String gameName, String rules) {
        GameTutorialDto gameTutorialDtoMocked = mock(GameTutorialDto.class);
        when(gameTutorialDtoMocked.getGameTutorialId()).thenReturn(uuidGameTutorial);
        when(gameTutorialDtoMocked.getGameId()).thenReturn(uuidGameDocument);
        when(gameTutorialDtoMocked.getGameName()).thenReturn(gameName);
        when(gameTutorialDtoMocked.getRules()).thenReturn(rules);
        return gameTutorialDtoMocked;
    }
}