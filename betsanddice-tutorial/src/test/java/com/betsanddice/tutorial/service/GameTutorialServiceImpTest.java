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

    UUID uuidGameTutorial = UUID.fromString("1682b3e9-056a-45b7-a0e9-eaf1e11775ad");
    UUID uuidGameDocument = UUID.fromString("c9de85c0-541e-48e6-b8ac-a9b2541231e3");
    GameTutorialDto gameTutorialDtoToAdd = new GameTutorialDto();
    GameTutorialDto gameTutorialDto = new GameTutorialDto();
    GameTutorialDocument gameTutorialDocument = new GameTutorialDocument();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        gameTutorialDtoToAdd = new GameTutorialDto("nonExistingGameName", "rules");
        gameTutorialDto = new GameTutorialDto(uuidGameTutorial, uuidGameDocument,"nonExistingGameName", "rules");
        gameTutorialDocument = new GameTutorialDocument(uuidGameTutorial, uuidGameDocument,"nonExistingGameName", "rules");
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
        when(gameTutorialRepository.findByUuid(uuidGameTutorial)).thenReturn(Mono.just(gameTutorialDocument));
        when(converter.fromDocumentToDto(gameTutorialDocument)).thenReturn(gameTutorialDto);

        Mono<GameTutorialDto> resultDto = gameTutorialService.getGameTutorialByUuid(uuidGameTutorial.toString());

        StepVerifier.create(resultDto)
                .expectNext(gameTutorialDto)
                .expectComplete()
                .verify();

        verify(gameTutorialRepository).findByUuid(uuidGameTutorial);
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
        when(gameTutorialRepository.findByUuid(uuidGameTutorial)).thenReturn(Mono.empty());

        Mono<GameTutorialDto> result = gameTutorialService.getGameTutorialByUuid(uuidGameTutorial.toString());

        StepVerifier.create(result)
                .expectError(GameTutorialNotFoundException.class)
                .verify();

        verify(gameTutorialRepository).findByUuid(uuidGameTutorial);
        verifyNoInteractions(converter);
    }

    @Test
    void getGameTutorialByGameId_ValidUuid_GameTutorialFound() {
        when(gameTutorialRepository.findByGameId(uuidGameDocument)).thenReturn(Mono.just(gameTutorialDocument));
        when(converter.fromDocumentToDto(gameTutorialDocument)).thenReturn(gameTutorialDto);

        Mono<GameTutorialDto> resultDto = gameTutorialService.getGameTutorialByGameId(uuidGameDocument.toString());

        StepVerifier.create(resultDto)
                .expectNext(gameTutorialDto)
                .expectComplete()
                .verify();

        verify(gameTutorialRepository).findByGameId(uuidGameDocument);
        verify(converter).fromDocumentToDto(gameTutorialDocument);
    }

    @Test
    void getGameTutorialByGameId_InvalidUuid_ErrorThrown() {
        String invalidId = "invalid-uuid";

        Mono<GameTutorialDto> result = gameTutorialService.getGameTutorialByGameId(invalidId);

        StepVerifier.create(result)
                .expectError(BadUuidException.class)
                .verify();

        verifyNoInteractions(gameTutorialRepository);
        verifyNoInteractions(converter);
    }

    @Test
    void getGameTutorialByGameId_NonExistentUuid_ErrorThrown() {
        when(gameTutorialRepository.findByGameId(uuidGameDocument)).thenReturn(Mono.empty());

        Mono<GameTutorialDto> result = gameTutorialService.getGameTutorialByGameId(uuidGameDocument.toString());

        StepVerifier.create(result)
                .expectError(GameTutorialNotFoundException.class)
                .verify();

        verify(gameTutorialRepository).findByGameId(uuidGameDocument);
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

    @Test
    void validateUuid_ValidUuid_ReturnsMonoWithUuid_Test() {
        String validUuid = "550e8400-e29b-41d4-a716-446655440000";

        Mono<UUID> resultMono = gameTutorialService.validateUuid(validUuid);

        StepVerifier.create(resultMono)
                .expectNextMatches(uuid -> uuid.toString().equals(validUuid))
                .expectComplete()
                .verify();
    }

    @Test
    void validateUuid_InvalidUuid_ReturnsErrorMono_Test() {
        String invalidUuid = "invalid-uuid";

        Mono<UUID> resultMono = gameTutorialService.validateUuid(invalidUuid);

        StepVerifier.create(resultMono)
                .expectError(BadUuidException.class)
                .verify();
    }

    @Test
    void validateUuid_EmptyUuid_ReturnsErrorMono_Test() {
        String emptyUuid = "";

        Mono<UUID> resultMono = gameTutorialService.validateUuid(emptyUuid);

        StepVerifier.create(resultMono)
                .expectError(BadUuidException.class)
                .verify();
    }

    @Test
    void validateUuid_NullUuid_ReturnsErrorMono_Test() {
        Mono<UUID> resultMono = gameTutorialService.validateUuid(null);

        StepVerifier.create(resultMono)
                .expectError(BadUuidException.class)
                .verify();
    }
}