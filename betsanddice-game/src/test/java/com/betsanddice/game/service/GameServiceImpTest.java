package com.betsanddice.game.service;

import com.betsanddice.game.document.GameDocument;
import com.betsanddice.game.dto.GameDto;
import com.betsanddice.game.exception.BadUuidException;
import com.betsanddice.game.exception.GameNotFoundException;
import com.betsanddice.game.helper.DocumentToDtoConverter;
import com.betsanddice.game.repository.GameRepository;
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

class GameServiceImpTest {
    @Mock
    private GameRepository gameRepository;

    @Mock
    private DocumentToDtoConverter<GameDocument, GameDto> converter = new DocumentToDtoConverter<>();

    @InjectMocks
    public GameServiceImp gameService;

    UUID uuidGame = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf3d3");
    UUID uuidTutorial = UUID.fromString("990b9699-75cf-42b5-aa0f-cf3a2169c770");
    UUID uuidStat = UUID.fromString("76cb3a63-bc48-4b68-a291-18140a3794d7");

    GameDocument gameDocument = new GameDocument(uuidGame, "Craps", uuidTutorial, uuidStat);
    GameDto gameDto = new GameDto(uuidGame, "Craps", uuidTutorial, uuidStat);

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getGameByUuid_ValidUuid_GameFound() {
        when(gameRepository.findByUuid(uuidGame)).thenReturn(Mono.just(gameDocument));
        when(converter.fromDocumentToDto(gameDocument, GameDto.class)).thenReturn(gameDto);

        Mono<GameDto> resultDto = gameService.getGameByUuid(uuidGame.toString());

        StepVerifier.create(resultDto)
                .expectNext(gameDto)
                .expectComplete()
                .verify();

        verify(gameRepository).findByUuid(uuidGame);
        verify(converter).fromDocumentToDto(gameDocument, GameDto.class);
    }

    @Test
    void getGameByUuid_InvalidUuid_ErrorThrown() {
        String invalidId = "invalid-uuid";

        Mono<GameDto> result = gameService.getGameByUuid(invalidId);

        StepVerifier.create(result)
                .expectError(BadUuidException.class)
                .verify();

        verifyNoInteractions(gameRepository);
        verifyNoInteractions(converter);
    }

    @Test
    void getGameByUuid_NonExistentUuid_ErrorThrown() {
        when(gameRepository.findByUuid(uuidGame)).thenReturn(Mono.empty());

        Mono<GameDto> result = gameService.getGameByUuid(uuidGame.toString());

        StepVerifier.create(result)
                .expectError(GameNotFoundException.class)
                .verify();

        verify(gameRepository).findByUuid(uuidGame);
        verifyNoInteractions(converter);
    }

    @Test
    void getAllCrapsGames_CrapsGamesExist_CrapsGamesReturned() {
        GameDto gameDto1 = new GameDto();
        GameDto gameDto2 = new GameDto();
        GameDto[] expectedGames = {gameDto1, gameDto2};

        when(gameRepository.findAll()).thenReturn(Flux.just(new GameDocument(), new GameDocument()));
        when(converter.fromDocumentFluxToDtoFlux(any(), any())).thenReturn(Flux.just(gameDto1, gameDto2));

        Flux<GameDto> result = gameService.getAllGames();

        StepVerifier.create(result)
                .expectNext(expectedGames)
                .expectComplete()
                .verify();

        verify(gameRepository).findAll();
        verify(converter).fromDocumentFluxToDtoFlux(any(), any());
    }
}