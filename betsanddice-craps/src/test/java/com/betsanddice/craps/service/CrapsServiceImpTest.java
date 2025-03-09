package com.betsanddice.craps.service;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.dto.BetDto;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.dto.GenericResultDto;
import com.betsanddice.craps.exception.BadUuidException;
import com.betsanddice.craps.exception.CrapsGameNotFoundException;
import com.betsanddice.craps.helper.DocumentToDtoConverter;
import com.betsanddice.craps.repository.CrapsGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
class CrapsServiceImpTest {

    @Mock
    private CrapsGameRepository crapsGameRepository;

    @Mock
    private DocumentToDtoConverter<CrapsGameDocument, CrapsGameDto> converter;

    @InjectMocks
    private CrapsGameServiceImp crapsGameService;

    @BeforeEach
    void setup() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlayCrapsGameByUserValidUuid() {
        String userUuid = "706507d4-b89f-41eb-a7eb-41838d08a08f";

        CrapsGameDocument crapsGameDocument = new CrapsGameDocument();
        crapsGameDocument.setUserId(UUID.fromString(userUuid));

        CrapsGameDto crapsGameDto = new CrapsGameDto();

        BetDto betDto = new BetDto(7, 3, 10.0);

        when(crapsGameRepository.save(any(CrapsGameDocument.class))).thenReturn(Mono.just(crapsGameDocument));
        when(converter.fromDocumentToDto(any(CrapsGameDocument.class), any(Class.class))).thenReturn(crapsGameDto);

        Mono<CrapsGameDto> result = crapsGameService.playAndBetCrapsGameByUser(userUuid, betDto);

        StepVerifier.create(result)
                .assertNext(resultDto -> assertThat(resultDto).usingRecursiveComparison().isEqualTo(crapsGameDto))
                .verifyComplete();
    }

    @Test
    void testPlayCrapsGameByUserInvalidUuid() {
        String userUuid = "invalid-uuid";
        BetDto betDto = new BetDto(7, 3, 10.0);

        Mono<CrapsGameDto> result = crapsGameService.playAndBetCrapsGameByUser(userUuid, betDto);

        StepVerifier.create(result)
                .expectError(BadUuidException.class)
                .verify();

    }

    //TODO testGetCrapsGameByUser_CrapsGamesExist_CrapsGamesReturned()

//    @Test
//    void getCrapsGameByUser_CrapsGamesExist_CrapsGamesReturned() {
//        String userUuid = "706507d4-b89f-41eb-a7eb-41838d08a08f";
//
//        int expectedDiceSum = 7;
//        int expectedAttempts = 3;
//        double amountBet = 10.0;
//
//        BetDto betDto = new BetDto (expectedDiceSum, expectedAttempts, amountBet);
//
//        CrapsGameDocument crapsGameDocument1 = new CrapsGameDocument(UUID.randomUUID(), UUID.fromString(userUuid),
//                LocalDateTime.now(), betDto.getExpectedAttempts(), betDto.getExpectedAttempts(),
//                betDto.getAmountBet(), List.of(new DiceRollDocument(1, 2)));
//
//        CrapsGameDocument crapsGameDocument2 = new CrapsGameDocument(UUID.randomUUID(), UUID.fromString(userUuid),
//                LocalDateTime.now(), betDto.getExpectedAttempts(), betDto.getExpectedAttempts(),
//                betDto.getAmountBet(), List.of(new DiceRollDocument(1, 2)));
//
//        int attempts = 1;
//        boolean isWon = false;
//        double bettingOdds = 1.0;
//        double amountReturned = -10.0;
//
//        ResultCrapsGameDto resultCrapsGameDto= new ResultCrapsGameDto(attempts, isWon, bettingOdds, amountReturned);
//
//        CrapsGameDto crapsGameDto1 = new CrapsGameDto(crapsGameDocument1.getUuid(), crapsGameDocument1.getUserId(),
//                String.valueOf(crapsGameDocument1.getDate()), crapsGameDocument1.getExpectedAttempts(),
//                crapsGameDocument1.getExpectedAttempts(), crapsGameDocument1.getAmountBet(),
//                crapsGameDocument1.getDiceRollsList(), resultCrapsGameDto);
//
//        CrapsGameDto crapsGameDto2 = new CrapsGameDto(crapsGameDocument2.getUuid(), crapsGameDocument2.getUserId(),
//                String.valueOf(crapsGameDocument2.getDate()), crapsGameDocument2.getExpectedAttempts(),
//                crapsGameDocument2.getExpectedAttempts(), crapsGameDocument2.getAmountBet(),
//                crapsGameDocument2.getDiceRollsList(), resultCrapsGameDto);
//
//        int offset = 1;
//        int limit = 2;
//
//        when(crapsGameRepository.findByUserId(UUID.fromString(userUuid)))
//                .thenReturn(Flux.just(crapsGameDocument1, crapsGameDocument2));
//        when(converter.fromDocumentFluxToDtoFlux(any(), any()))
//                .thenReturn(Flux.just(crapsGameDto1, crapsGameDto2));
//        when(crapsGameRepository.count()).thenReturn(Mono.just(100L));
//
//        Mono<GenericResultDto<CrapsGameDto>> result = crapsGameService.getCrapsGameByUser(String.valueOf(crapsGameDocument1.getUserId()),
//                offset, limit);
//
//        verify(crapsGameRepository).findByUserId(crapsGameDocument1.getUserId());
//        verify(converter).fromDocumentFluxToDtoFlux(any(), any());
//
//        StepVerifier.create(result)
//                .expectSubscription()
//                .assertNext(resultDto -> {
//                    Assertions.assertEquals(100, resultDto.getCount());
//                    Assertions.assertEquals(offset, resultDto.getOffset());
//                    Assertions.assertEquals(limit, resultDto.getLimit());
//                    Assertions.assertEquals(2, resultDto.getResults().length);
//                    Assertions.assertEquals(crapsGameDto1, resultDto.getResults()[0]);
//                    Assertions.assertEquals(crapsGameDto1, resultDto.getResults()[1]);
//                })
//                .expectComplete()
//                .verify();
//    }


    @Test
    void testGetCrapsGameByUser_WhenNoGamesExist_ThrowsException() {
        String userId = "123e4567-e89b-12d3-a456-426614174000";
        int offset = 0;
        int limit = 2;

        when(crapsGameRepository.findByUserId(UUID.fromString(userId))).thenReturn(Flux.empty());

        Mono<GenericResultDto<CrapsGameDto>> result = crapsGameService.getCrapsGameByUser(userId, offset, limit);

        StepVerifier.create(result)
                .expectError(CrapsGameNotFoundException.class)
                .verify();
    }
}