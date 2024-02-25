package com.betsanddice.game.service;

import com.betsanddice.game.document.CrapsGameDocument;
import com.betsanddice.game.dto.CrapsGameDto;
import com.betsanddice.game.dto.DiceRollDto;
import com.betsanddice.game.helper.CrapsGameDocumentToDtoConverter;
import com.betsanddice.game.repository.CrapsGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CrapsGameServiceImpTest {
    @Mock
    private CrapsGameRepository crapsGameRepository;

    @Mock
    private CrapsGameDocumentToDtoConverter documentToDtoConverter;

    @InjectMocks
    public CrapsGameServiceImp crapsGameService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCrapsGames_CrapsGamesExist_CrapsGamesReturned() {
        CrapsGameDto crapsGameDto1 = new CrapsGameDto();
        CrapsGameDto crapsGameDto2 = new CrapsGameDto();
        CrapsGameDto[] expectedCrapsGames = {crapsGameDto1, crapsGameDto2};

        when(crapsGameRepository.findAll()).thenReturn(Flux.just(new CrapsGameDocument(), new CrapsGameDocument()));
        when(documentToDtoConverter.fromDocumentFluxToDtoFlux(any())).thenReturn(Flux.just(crapsGameDto1, crapsGameDto2));

        Flux<CrapsGameDto> result = crapsGameService.getAllCrapsGame();

        StepVerifier.create(result)
                .expectNext(expectedCrapsGames)
                .expectComplete()
                .verify();

        verify(crapsGameRepository).findAll();
        verify(documentToDtoConverter).fromDocumentFluxToDtoFlux(any());
    }

    @Test
    void testAddCrapsGameToUser() {
        UUID uuidCrapsGame = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf3d3");

        UUID uuidUser = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

        DiceRollDto diceRollDto1 = new DiceRollDto(1, 2, 3);
        DiceRollDto diceRollDto2 = new DiceRollDto(3, 4, 7);
        List<DiceRollDto> diceRollsList = List.of(diceRollDto1, diceRollDto2);

        CrapsGameDto crapsGameDto = new CrapsGameDto(uuidCrapsGame, uuidUser, "2023-01-31 12:00:00", 2, diceRollsList);

        when(crapsGameRepository.save(any())).thenReturn(Mono.empty());
        when(documentToDtoConverter.fromDocumentToDto(any())).thenReturn(crapsGameDto);

        Mono<CrapsGameDto> resultMono = crapsGameService.addCrapsGameToUser(String.valueOf(uuidUser));

        StepVerifier.create(resultMono)
                .expectNext(crapsGameDto)
                .expectComplete()
                .verify();

        verify(crapsGameRepository, times(1)).save(any());
        verify(documentToDtoConverter, times(1)).fromDocumentToDto(any());
    }

}