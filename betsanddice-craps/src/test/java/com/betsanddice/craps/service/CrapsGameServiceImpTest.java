package com.betsanddice.craps.service;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.helper.DocumentToDtoConverter;
import com.betsanddice.craps.repository.CrapsGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CrapsGameServiceImpTest {
    @Mock
    private CrapsGameRepository crapsGameRepository;

    @Mock
    private DocumentToDtoConverter converter;

    @InjectMocks
    public CrapsGameServiceImp crapsGameService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCrapsGames_CrapsGamesExist_CrapsGamesReturned() {

        UUID uuidCrapsGame1 = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf3d3");
        UUID uuidCrapsGame2 = UUID.fromString("6160a07c-1d0f-4ac0-80b0-ef8f17bcad53");

        UUID uuidUser = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

        int[][] diceRolls = new int[][]{{1, 2}, {4, 5}, {3, 4}};

        CrapsGameDocument crapsGameDocument1 = new CrapsGameDocument(uuidCrapsGame1, uuidUser, "Player1",
                LocalDateTime.now(), diceRolls);
        CrapsGameDocument crapsGameDocument2 = new CrapsGameDocument(uuidCrapsGame2, uuidUser, "Player1",
                LocalDateTime.now(), diceRolls);

        CrapsGameDto crapsGameDto1 = new CrapsGameDto(uuidCrapsGame1, uuidUser, "Player1",
                LocalDateTime.now().toString(), diceRolls);
        CrapsGameDto crapsGameDto2 = new CrapsGameDto(uuidCrapsGame2, uuidUser, "Player1",
                LocalDateTime.now().toString(), diceRolls);

        CrapsGameDto[] expectedCrapsGames = {crapsGameDto1, crapsGameDto2};

        when(crapsGameRepository.findAll()).thenReturn(Flux.just(crapsGameDocument1, crapsGameDocument2));
        when(converter.fromDocumentFluxToDtoFlux(any(), any())).thenReturn(Flux.just(crapsGameDto1, crapsGameDto2));

        Flux<CrapsGameDto> result = crapsGameService.getAllCrapsGame();

        StepVerifier.create(result)
                .expectNext(expectedCrapsGames)
                .expectComplete()
                .verify();

        verify(crapsGameRepository).findAll();
        verify(converter).fromDocumentFluxToDtoFlux(any(), any());
    }

}