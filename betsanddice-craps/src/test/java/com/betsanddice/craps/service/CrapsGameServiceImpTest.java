package com.betsanddice.craps.service;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.helper.CrapsGameDocumentToDtoConverter;
import com.betsanddice.craps.repository.CrapsGameRepository;
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

class CrapsGameServiceImpTest {
    @Mock
    private CrapsGameRepository crapsGameRepository;

    @Mock
    private CrapsGameDocumentToDtoConverter converter;

    @InjectMocks
    public CrapsGameServiceImp crapsGameService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCrapsGames_CrapsGamesExist_CrapsGamesReturned() {

/*        UUID uuidCrapsGame1 = UUID.fromString("50feba3c-3cbf-48ad-8142-cccf7c6bf3d3");
        UUID uuidCrapsGame2 = UUID.fromString("6160a07c-1d0f-4ac0-80b0-ef8f17bcad53");

        UUID uuidUser = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

        UUID uuid1 = UUID.fromString("c341527c-6379-4d8a-a885-c938b121fb75");
        UUID uuid2 = UUID.fromString("5c12481c-e571-4808-a7dc-a9247e5c1037");
        List<UUID> diceRollsList = List.of(uuid1, uuid2);

        CrapsGameDocument crapsGameDocument1 = new CrapsGameDocument(uuidCrapsGame1, uuidUser, "Player1",
                LocalDateTime.now(), diceRollsList);
        CrapsGameDocument crapsGameDocument2 = new CrapsGameDocument(uuidCrapsGame2, uuidUser, "Player1",
                LocalDateTime.now(), diceRollsList);

        CrapsGameDto crapsGameDto1 = new CrapsGameDto(uuidCrapsGame1, uuidUser, "Player1",
                LocalDateTime.now().toString(), diceRollsList);
        CrapsGameDto crapsGameDto2 = new CrapsGameDto(uuidCrapsGame2, uuidUser, "Player1",
                LocalDateTime.now().toString(), diceRollsList);

        CrapsGameDto[] expectedCrapsGames = {crapsGameDto1, crapsGameDto2};*/



/*        when(challengeRepository.findAllByUuidNotNull(pageable))
                .thenReturn(Flux.just(new ChallengeDocument(), new ChallengeDocument()));
        when(challengeConverter.convertDocumentFluxToDtoFlux(any(), any())).thenReturn(Flux.just(challengeDto3, challengeDto4));

        // Act
        Flux<ChallengeDto> result = challengeService.getAllChallenges(1, 2);

        // Assert
        StepVerifier.create(result)
                .expectNext(expectedChallengesPaged)
                .expectComplete()
                .verify();

        verify(challengeRepository).findAllByUuidNotNull(pageable);
        verify(challengeConverter).convertDocumentFluxToDtoFlux(any(), any());*/

        CrapsGameDto crapsGameDto1 = new CrapsGameDto();
        CrapsGameDto crapsGameDto2 = new CrapsGameDto();
        CrapsGameDto[] expectedCrapsGames = {crapsGameDto1, crapsGameDto2};

        when(crapsGameRepository.findAll()).thenReturn(Flux.just(new CrapsGameDocument(), new CrapsGameDocument()));
        when(converter.fromDocumentFluxToDtoFlux(any())).thenReturn(Flux.just(crapsGameDto1, crapsGameDto2));

        Flux<CrapsGameDto> result = crapsGameService.getAllCrapsGame();

        StepVerifier.create(result)
                .expectNext(expectedCrapsGames)
                .expectComplete()
                .verify();

        verify(crapsGameRepository).findAll();
        verify(converter).fromDocumentFluxToDtoFlux(any());
    }

}