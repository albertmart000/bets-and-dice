package com.betsanddice.tutorial.service;

import com.betsanddice.tutorial.document.GameTutorialDocument;
import com.betsanddice.tutorial.dto.GameTutorialDto;
import com.betsanddice.tutorial.helper.GameTutorialDocumentToDtoConverter;
import com.betsanddice.tutorial.repository.GameTutorialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GameTutorialServiceImpTest {

    @Mock
    private GameTutorialRepository gameTutorialRepository;

    @Mock
    private GameTutorialDocumentToDtoConverter converter;

    @InjectMocks
    private GameTutorialServiceImp gameTutorialService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getGameTutorialByUuid_ValidUuid_GameTutorialFound() {
        UUID gameTutorialUuid = UUID.randomUUID();
        GameTutorialDocument gameTutorialDocument = new GameTutorialDocument();
        GameTutorialDto gameTutorialDto = new GameTutorialDto();

        when(gameTutorialRepository.findByUuid(gameTutorialUuid))
                .thenReturn(Mono.just(gameTutorialDocument));
        when(converter.fromDocumentToDto(gameTutorialDocument))
                .thenReturn(gameTutorialDto);

        Mono<GameTutorialDto> resultDto = gameTutorialService.getGameTutorialByUuid(gameTutorialUuid.toString());

        StepVerifier.create(resultDto)
                .expectNext(gameTutorialDto)
                .expectComplete()
                .verify();

        verify(gameTutorialRepository).findByUuid(gameTutorialUuid);
        verify(converter).fromDocumentToDto(gameTutorialDocument);
    }

    @Test
    void getAllGameTutorials_GameTutorialsExist_GamesTutorialsReturned() {
        GameTutorialDto gameTutorialDto1 = new GameTutorialDto();
        GameTutorialDto gameTutorialDto2 = new GameTutorialDto();
        GameTutorialDto[] expectedGameTutorials = {gameTutorialDto1, gameTutorialDto2};

        when(gameTutorialRepository.findAll()).thenReturn(Flux.just(new GameTutorialDocument(), new GameTutorialDocument()));
        when(converter.fromDocumentFluxToDtoFlux(any())).thenReturn(Flux.just(gameTutorialDto1, gameTutorialDto2));

        Flux<GameTutorialDto> result = gameTutorialService.getAllGameTutorials();

        StepVerifier.create(result)
                .expectNext(expectedGameTutorials)
                .expectComplete()
                .verify();

        verify(gameTutorialRepository).findAll();
        verify(converter).fromDocumentFluxToDtoFlux(any());
    }

/*    @Test
    void testAddGameTutorial() {
        UUID uuidGameTutorialDocument1 = UUID.fromString("660e1b18-0c0a-4262-a28a-85de9df6ac5f");
        GameTutorialDto gameTutorialDto = new GameTutorialDto("Name", "rules");

        GameTutorialDto gameTutorialDtoToAdd = new GameTutorialDto(uuidGameTutorialDocument1, gameTutorialDto.getGameName(),
                gameTutorialDto.getRules());

        when(gameTutorialRepository.save(any())).thenReturn(Mono.empty());
        when(converter.fromDocumentToDto(any())).thenReturn(gameTutorialDtoToAdd);

        Mono<GameTutorialDto> resultMono = gameTutorialService.addGameTutorial(gameTutorialDtoToAdd);

        StepVerifier.create(resultMono)
                .expectNext(gameTutorialDtoToAdd)

                .expectComplete()
                .verify();

        verify(gameTutorialRepository, times(1)).save(any());
        verify(converter, times(1)).fromDocumentToDto(any());
    }*/

}