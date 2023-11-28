package com.betsanddice.tutorial.service;

import com.betsanddice.tutorial.document.GameTutorialDocument;
import com.betsanddice.tutorial.dto.GameTutorialDto;
import com.betsanddice.tutorial.exception.BadUuidException;
import com.betsanddice.tutorial.exception.GameTutorialAlreadyExistException;
import com.betsanddice.tutorial.exception.GameTutorialNotFoundException;
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

    UUID gameTutorialUuid = UUID.fromString("1682b3e9-056a-45b7-a0e9-eaf1e11775ad");

    GameTutorialDto gameTutorialDtoToAdd = new GameTutorialDto();
    GameTutorialDto gameTutorialDto = new GameTutorialDto();
    GameTutorialDocument gameTutorialDocument = new GameTutorialDocument();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        gameTutorialDtoToAdd = new GameTutorialDto("nonExistingGameName", "rules");
        gameTutorialDto = new GameTutorialDto(gameTutorialUuid, "nonExistingGameName", "rules");
        gameTutorialDocument = new GameTutorialDocument(gameTutorialUuid, "nonExistingGameName", "rules");

    }

    @Test
    void addGameTutorial_GameNameNonExist_GameTutorialAdded() {
        when(gameTutorialRepository.findByGameName("nonExistingGameName")).thenReturn(Mono.empty());
        when(gameTutorialRepository.save(any())).thenReturn(Mono.just(gameTutorialDocument));
        when(converter.fromDocumentToDto(any())).thenReturn(gameTutorialDto);

        Mono<GameTutorialDto> result = gameTutorialService.addGameTutorial(gameTutorialDtoToAdd);

        StepVerifier.create(result)
                .expectNext(gameTutorialDto)
                .expectComplete()
                .verify();

        verify(gameTutorialRepository, times(1)).save(any());
        verify(converter, times(1)).fromDocumentToDto(any());
    }

    @Test
    void addGameTutorial_GameNameExist_ErrorThrow() {
        gameTutorialDto.setGameName("existingGameName");

        when(gameTutorialRepository.findByGameName("existingGameName")).thenReturn(Mono.just(gameTutorialDocument));
        Mono<GameTutorialDto> result = gameTutorialService.addGameTutorial(gameTutorialDto);

        StepVerifier.create(result)
                .expectError(GameTutorialAlreadyExistException.class)
                .verify();
    }

    @Test
    void getGameTutorialByUuid_ValidUuid_GameTutorialFound() {
        when(gameTutorialRepository.findByUuid(gameTutorialUuid)).thenReturn(Mono.just(gameTutorialDocument));
        when(converter.fromDocumentToDto(gameTutorialDocument)).thenReturn(gameTutorialDto);

        Mono<GameTutorialDto> resultDto = gameTutorialService.getGameTutorialByUuid(gameTutorialUuid.toString());

        StepVerifier.create(resultDto)
                .expectNext(gameTutorialDto)
                .expectComplete()
                .verify();

        verify(gameTutorialRepository).findByUuid(gameTutorialUuid);
        verify(converter).fromDocumentToDto(gameTutorialDocument);
    }

    @Test
    void getGameTutorialByUuid_InvalidUuid_ErrorThrown() {
        String invalidId = "invalid-uuid";

        Mono<GameTutorialDto> result = gameTutorialService.getGameTutorialByUuid(invalidId);

        StepVerifier.create(result)
                .expectError(BadUuidException.class)
                .verify();

        verifyNoInteractions(gameTutorialRepository);
        verifyNoInteractions(converter);
    }

    @Test
    void getGameTutorialByUuid_NonExistentUuid_ErrorThrown() {
        when(gameTutorialRepository.findByUuid(gameTutorialUuid)).thenReturn(Mono.empty());

        Mono<GameTutorialDto> result = gameTutorialService.getGameTutorialByUuid(gameTutorialUuid.toString());

        StepVerifier.create(result)
                .expectError(GameTutorialNotFoundException.class)
                .verify();

        verify(gameTutorialRepository).findByUuid(gameTutorialUuid);
        verifyNoInteractions(converter);
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

}