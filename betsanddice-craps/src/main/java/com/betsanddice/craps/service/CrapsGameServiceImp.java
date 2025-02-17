package com.betsanddice.craps.service;

import com.betsanddice.craps.document.BetDocument;
import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.document.DiceRollDocument;
import com.betsanddice.craps.dto.ResultDto;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.exception.BadUuidException;
import com.betsanddice.craps.helper.DocumentToDtoConverter;
import com.betsanddice.craps.repository.CrapsGameRepository;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class CrapsGameServiceImp implements ICrapsGameService {

    private static final Logger log = LoggerFactory.getLogger(CrapsGameServiceImp.class);
    private static final Pattern UUID_FORM = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    private final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    private CrapsGameRepository crapsGameRepository;

    @Autowired
    private DocumentToDtoConverter<CrapsGameDocument, CrapsGameDto> converter = new DocumentToDtoConverter<>();

    public Mono<CrapsGameDto> playCrapsGameByUser(String uuid, int expectedDiceSum) {
        return validateUuid(uuid)
                .flatMap(userUuid -> generateDiceRollsList(expectedDiceSum)
                        .map(diceRollsList -> CrapsGameDocument.builder()
                                .uuid(UUID.randomUUID())
                                .userId(userUuid)
                                .date(LocalDateTime.now())
                                .diceRollsList(diceRollsList)
                                .build())
                        .flatMap(crapsGameDocument -> crapsGameRepository.save(crapsGameDocument))
                        .map(documentToSave -> converter.fromDocumentToDto(documentToSave, CrapsGameDto.class)))
                .doOnSuccess(crapsGameDto -> log.info("Successfully played CrapsGame by user with ID: {}", uuid))
                .doOnError(error -> log.error("Operation failed with error message: {}", error.getMessage()));
    }

    @Override
    public Mono<CrapsGameDto> playAndBetCrapsGameByUser(String userUuid, double amountBet,
                                                        int expectedDiceSum, int expectedAttempts) {
        return validateUuid(userUuid)
                .flatMap(uuid -> generateDiceRollsList(expectedDiceSum)
                        .flatMap(diceRollsList -> {
                            ResultDto result = generateResultDto(amountBet, expectedDiceSum, expectedAttempts,
                                    diceRollsList);
                            return crapsGameRepository.save(buildCrapsGameDocument(userUuid, amountBet, expectedDiceSum,
                                            expectedAttempts, diceRollsList))
                                    .map(crapsGameDocumentToSave -> {
                                        CrapsGameDto crapsGameDto = converter.fromDocumentToDto(crapsGameDocumentToSave, CrapsGameDto.class);
                                        crapsGameDto.setResult(result);
                                        return crapsGameDto;
                                    });
                        }))
                .doOnSuccess(crapsGameDto -> log.info("Successfully played CrapsGame with Bet by user with ID: {}", userUuid))
                .doOnError(error -> log.error("Operation failed with error message: {}", error.getMessage()));
    }

    private Mono<List<DiceRollDocument>> generateDiceRollsList(int expectedDiceSum) {
        return Flux.<DiceRollDocument>generate(flux -> {
                    int dice1 = secureRandom.nextInt(6) + 1;
                    int dice2 = secureRandom.nextInt(6) + 1;
                    int diceSum = dice1 + dice2;
                    flux.next(new DiceRollDocument(dice1, dice2));
                    if (diceSum == expectedDiceSum) {
                        flux.complete();
                    }
                })
                .collectList();
    }

    private double calculateOdd(int expectedDiceSum, int attempts) {
        if (expectedDiceSum < 2 || expectedDiceSum > 12) {
            return 0.0;
        }
        int favorableCombinations = Math.min(expectedDiceSum - 1, 13 - expectedDiceSum);
        double probSum = favorableCombinations / 36.0;
        double probNotSum = Math.pow(1 - probSum, attempts);
        double probResultAndAttempts = 1 - probNotSum;
        return (1 / probResultAndAttempts);
    }

    private ResultDto generateResultDto(double amountBet, int expectedDiceSum, int expectedAttempts, List<DiceRollDocument> diceRolls) {
        int attempts = diceRolls.size();
        boolean isWon = expectedDiceSum >= attempts;
        double bettingOdds = calculateOdd(expectedDiceSum, expectedAttempts);
        BigDecimal amountReturned = BigDecimal.valueOf(amountBet * (isWon ? bettingOdds : -1));

        return ResultDto.builder()
                .attempts(attempts)
                .playerWins(isWon)
                .bettingOdds(bettingOdds)
                .amountReturned(amountReturned)
                .build();
    }

    private CrapsGameDocument buildCrapsGameDocument(String userUuid, double amountBet, int expectedDiceSum,
                                                      int expectedAttempts, List<DiceRollDocument> diceRollsList) {
        BetDocument bet= BetDocument.builder()
                .expectedDiceSum(expectedDiceSum)
                .expectedAttempts(expectedAttempts)
                .amountBet(BigDecimal.valueOf(amountBet))
                .build();

        return CrapsGameDocument.builder()
                .uuid(UUID.randomUUID())
                .userId(UUID.fromString(userUuid))
                .date(LocalDateTime.now())
                .bet(bet)
                .diceRollsList(diceRollsList)
                .build();
    }

    private Mono<UUID> validateUuid(String id) {
        boolean validUuid = !StringUtils.isEmpty(id) && UUID_FORM.matcher(id).matches();
        if (!validUuid) {
            log.warn("Invalid ID format: {}", id);
            return Mono.error(new BadUuidException("Invalid ID format. Please indicate the correct format."));
        }
        return Mono.just(UUID.fromString(id));
    }
}