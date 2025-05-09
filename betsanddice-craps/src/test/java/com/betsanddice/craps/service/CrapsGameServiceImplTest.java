package com.betsanddice.craps.service;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.document.DiceRollDocument;
import com.betsanddice.craps.dto.*;
import com.betsanddice.craps.exception.BadUuidException;
import com.betsanddice.craps.exception.CrapsGameNotFoundException;
import com.betsanddice.craps.helper.DocumentToDtoConverter;
import com.betsanddice.craps.repository.CrapsGameRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class CrapsGameServiceImplTest {

    @Mock
    private CrapsGameRepository crapsGameRepository;

    @Mock
    private DocumentToDtoConverter<CrapsGameDocument, CrapsGameDto> converter;

    @InjectMocks
    private CrapsGameServiceImpl crapsGameService;

    private CrapsGameDocument crapsGameDocument;
    private CrapsGameDocument crapsGameDocument1;
    private CrapsGameDto crapsGameDto;
    private CrapsGameDto crapsGameDto1;

    private BetDto betDto;
    private UserCrapsGameStatsDto userCrapsGameStatsDto;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        int expectedDiceSum = 7;
        int expectedAttempts = 1;
        double amountBet = 10.0;

        betDto = new BetDto(expectedDiceSum, expectedAttempts, amountBet);

        String userUuid = "706507d4-b89f-41eb-a7eb-41838d08a08f";

        UUID crapsGameRandomId = UUID.randomUUID();
        UUID crapsGameRandomId1 = UUID.randomUUID();
        LocalDateTime localDateTime = LocalDateTime.of(2023, 6, 5, 12, 30, 0);

        List<DiceRollDocument> diceRollsList = List.of(
                new DiceRollDocument(1, 2),
                new DiceRollDocument(3, 4));

        int attempts = 1;
        boolean isWon = false;
        double bettingOdds = 6.0;
        double amountReturned = -10.0;

        ResultCrapsGameDto resultCrapsGameDto = new ResultCrapsGameDto(attempts, isWon, bettingOdds, amountReturned);

        crapsGameDocument = new CrapsGameDocument(crapsGameRandomId, UUID.fromString(userUuid),
                localDateTime, expectedDiceSum, expectedAttempts, amountBet, diceRollsList);

        crapsGameDocument1 = new CrapsGameDocument(crapsGameRandomId1, UUID.fromString(userUuid),
                localDateTime, expectedDiceSum, expectedAttempts, amountBet, diceRollsList);

        crapsGameDto = getCrapsGameDtoMocked(crapsGameRandomId, UUID.fromString(userUuid),
                "2023-06-05 12:30:00", expectedDiceSum, expectedAttempts, amountBet,
                diceRollsList, resultCrapsGameDto);

        crapsGameDto1 = getCrapsGameDtoMocked(crapsGameRandomId1, UUID.fromString(userUuid),
                "2023-06-05 12:30:00", expectedDiceSum, expectedAttempts, amountBet,
                diceRollsList, resultCrapsGameDto);

        String nameGame = "Craps";
        int gamesPlayed = 2;
        int gamesWon = 0;
        double percentGamesWon = 0.0;
        double totalAmountBet = 20.0;
        double profitObtained = -20.0;

        userCrapsGameStatsDto = new UserCrapsGameStatsDto(UUID.fromString(userUuid), nameGame,
                gamesPlayed, gamesWon, percentGamesWon, totalAmountBet, profitObtained);

    }


    @Test
    void testPlayCrapsGameByUser_ValidUuid() {
        String userUuid = "706507d4-b89f-41eb-a7eb-41838d08a08f";

        crapsGameDocument = new CrapsGameDocument();
        crapsGameDocument.setUserId(UUID.fromString(userUuid));

        crapsGameDto = new CrapsGameDto();

        betDto = new BetDto(7, 3, 10.0);

        when(crapsGameRepository.save(any(CrapsGameDocument.class))).thenReturn(Mono.just(crapsGameDocument));
        when(converter.fromDocumentToDto(any(CrapsGameDocument.class), any(Class.class))).thenReturn(crapsGameDto);

        Mono<CrapsGameDto> result = crapsGameService.playAndBetCrapsGameByUser(userUuid, betDto);

        StepVerifier.create(result)
                .assertNext(resultDto -> assertThat(resultDto).usingRecursiveComparison().isEqualTo(crapsGameDto))
                .verifyComplete();
    }

    @Test
    void testPlayCrapsGameByUser_InvalidUuid() {
        String userUuid = "invalid-uuid";

        Mono<CrapsGameDto> result = crapsGameService.playAndBetCrapsGameByUser(userUuid, betDto);

        StepVerifier.create(result)
                .expectError(BadUuidException.class)
                .verify();
    }

    @Test
    void testGetCrapsGameByUser_CrapsGamesExist_CrapsGamesReturned() {
        String userUuid = "706507d4-b89f-41eb-a7eb-41838d08a08f";

        int offset = 0;
        int limit = 1;

        when(crapsGameRepository.findByUserId(UUID.fromString(userUuid))).thenReturn(Flux.just(crapsGameDocument));
        when(converter.fromDocumentToDto(any(), any())).thenReturn(crapsGameDto);

        Mono<GenericResultDto<CrapsGameDto>> result = crapsGameService.getCrapsGameByUser(userUuid, offset, limit);

        StepVerifier.create(result)
                .assertNext(actualResult -> {
                    assertThat(actualResult.getResults()[0]).usingRecursiveComparison().isEqualTo(crapsGameDto);
                })
                .verifyComplete();
    }

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

//    @Test
//    void testGetUserCrapsGameStats() {
//        String userUuid = "706507d4-b89f-41eb-a7eb-41838d08a08f";
//
//        when(crapsGameRepository.findByUserId(UUID.fromString(userUuid)))
//                .thenReturn(Flux.just(crapsGameDocument, crapsGameDocument1));
//        when(converter.fromDocumentFluxToDtoFlux(any(), any()))
//                .thenReturn(Flux.just(crapsGameDto, crapsGameDto1));
//
//        Mono<UserCrapsGameStatsDto> result = crapsGameService.getUserCrapsGameStats(userUuid);
//
//        StepVerifier.create(result)
//                .assertNext(actualResult -> {
//                    assertThat(actualResult).isEqualTo(userCrapsGameStatsDto);
//                })
//                .verifyComplete();
//    }

    @Test
    void testGetCrapsGameStatsByUser_WhenNoGamesExist_ThrowsException() {
        String userUuid = "706507d4-b89f-41eb-a7eb-41838d08a08f";

        when(crapsGameRepository.findByUserId(UUID.fromString(userUuid))).thenReturn(Flux.empty());

        Mono<UserCrapsGameStatsDto> result = crapsGameService.getUserCrapsGameStats(userUuid);

        StepVerifier.create(result)
                .expectError(CrapsGameNotFoundException.class)
                .verify();
    }

    @Test
    void testDeleteCrapsGamesByUserId_Success() {
        String userUuid = "706507d4-b89f-41eb-a7eb-41838d08a08f";

        when(crapsGameRepository.findByUserId(UUID.fromString(userUuid))).thenReturn(Flux.just(crapsGameDocument, crapsGameDocument1));
        when(crapsGameRepository.deleteAll(List.of(crapsGameDocument, crapsGameDocument1))).thenReturn(Mono.empty());

        Mono<DeleteResponseDto> result = crapsGameService.deleteCrapsGamesByUserId(userUuid);

        StepVerifier.create(result)
                .assertNext(response -> {
                    Assertions.assertNotNull(response);
                    assertEquals(userUuid, response.getId());
                    assertEquals("CrapsGames deleted successfully.", response.getMessage());
                })
                .verifyComplete();

        verify(crapsGameRepository, times(1)).findByUserId(UUID.fromString(userUuid));
        verify(crapsGameRepository, times(1)).deleteAll(List.of(crapsGameDocument, crapsGameDocument1));
    }

    private CrapsGameDto getCrapsGameDtoMocked(UUID crapsGameRandomId, UUID uuid, String date, int expectedDiceSum,
                                               int expectedAttempts, double amountBet, List<DiceRollDocument> diceRollsList,
                                               ResultCrapsGameDto resultCrapsGameDto) {

        CrapsGameDto crapsGameDocMocked = mock(CrapsGameDto.class);
        when(crapsGameDocMocked.getUuid()).thenReturn(crapsGameRandomId);
        when(crapsGameDocMocked.getUserId()).thenReturn(uuid);
        when(crapsGameDocMocked.getDate()).thenReturn(date);
        when(crapsGameDocMocked.getExpectedDiceSum()).thenReturn(expectedDiceSum);
        when(crapsGameDocMocked.getExpectedAttempts()).thenReturn(expectedAttempts);
        when(crapsGameDocMocked.getAmountBet()).thenReturn(amountBet);
        when(crapsGameDocMocked.getDiceRollsList()).thenReturn(diceRollsList);
        when(crapsGameDocMocked.getResult()).thenReturn(resultCrapsGameDto);

        return crapsGameDocMocked;
    }

}