package com.betsanddice.craps.service;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.document.DiceRollDocument;
import com.betsanddice.craps.dto.BetDto;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.dto.ResultDto;
import com.betsanddice.craps.exception.BadUuidException;
import com.betsanddice.craps.helper.DocumentToDtoConverter;
import com.betsanddice.craps.repository.CrapsGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
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
        String userUuid = "123e4567-e89b-12d3-a456-426655440000";

        BetDto betDto = BetDto.builder()
                .expectedDiceSum(7)
                .expectedAttempts(3)
                .amountBet(10.0)
                .build();

        CrapsGameDocument crapsGameDocument = CrapsGameDocument.builder()
                .uuid(UUID.randomUUID())
                .userId(UUID.fromString(userUuid))
                .date(LocalDateTime.now())
                .expectedAttempts(betDto.getExpectedAttempts())
                .expectedAttempts(betDto.getExpectedAttempts())
                .amountBet(betDto.getAmountBet())
                .diceRollsList(List.of(new DiceRollDocument(1, 2)))
                .build();

        CrapsGameDto expectedCrapsGameDto = CrapsGameDto.builder()
                .uuid(crapsGameDocument.getUuid())
                .userId(crapsGameDocument.getUserId())
                .date(String.valueOf(crapsGameDocument.getDate()))
                .expectedAttempts(betDto.getExpectedAttempts())
                .expectedAttempts(betDto.getExpectedAttempts())
                .amountBet(betDto.getAmountBet())
                .diceRollsList(crapsGameDocument.getDiceRollsList())
                .result(ResultDto.builder()
                        .attempts(1)
                        .playerWins(false)
                        .bettingOdds(1.0)
                        .amountReturned(-10.0)
                        .build())
                .build();

        when(crapsGameRepository.save(any(CrapsGameDocument.class))).thenReturn(Mono.just(crapsGameDocument));
        when(converter.fromDocumentToDto(any(CrapsGameDocument.class), any(Class.class))).thenReturn(expectedCrapsGameDto);

        Mono<CrapsGameDto> result = crapsGameService.playAndBetCrapsGameByUser(userUuid, betDto);

        StepVerifier.create(result)
                .assertNext(crapsGameDto -> assertThat(crapsGameDto).usingRecursiveComparison().isEqualTo(expectedCrapsGameDto))
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
}