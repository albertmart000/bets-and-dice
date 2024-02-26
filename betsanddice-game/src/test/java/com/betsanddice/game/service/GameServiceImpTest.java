package com.betsanddice.game.service;

import com.betsanddice.game.document.GameDocument;
import com.betsanddice.game.dto.GameDto;
import com.betsanddice.game.helper.GameDocumentToDtoConverter;
import com.betsanddice.game.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameServiceImpTest {
    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameDocumentToDtoConverter documentToDtoConverter;

    @InjectMocks
    public GameServiceImp gameService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCrapsGames_CrapsGamesExist_CrapsGamesReturned() {
        GameDto gameDto1 = new GameDto();
        GameDto gameDto2 = new GameDto();
        GameDto[] expectedGames = {gameDto1, gameDto2};

        when(gameRepository.findAll()).thenReturn(Flux.just(new GameDocument(), new GameDocument()));
        when(documentToDtoConverter.fromDocumentFluxToDtoFlux(any())).thenReturn(Flux.just(gameDto1, gameDto2));

        Flux<GameDto> result = gameService.getAllGames();

        StepVerifier.create(result)
                .expectNext(expectedGames)
                .expectComplete()
                .verify();

        verify(gameRepository).findAll();
        verify(documentToDtoConverter).fromDocumentFluxToDtoFlux(any());
    }
}