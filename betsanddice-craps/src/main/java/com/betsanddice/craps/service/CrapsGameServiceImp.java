package com.betsanddice.craps.service;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.document.DiceRollDocument;
import com.betsanddice.craps.dto.*;
import com.betsanddice.craps.exception.BadUuidException;
import com.betsanddice.craps.exception.CrapsGameNotFoundException;
import com.betsanddice.craps.helper.DocumentToDtoConverter;
import com.betsanddice.craps.repository.CrapsGameRepository;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class CrapsGameServiceImp implements ICrapsGameService {

    private final Logger log = LoggerFactory.getLogger(CrapsGameServiceImp.class);
    private final Pattern UUID_FORM = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    private final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    private CrapsGameRepository crapsGameRepository;

    @Autowired
    private DocumentToDtoConverter<CrapsGameDocument, CrapsGameDto> crapsGameDocumentConverter = new DocumentToDtoConverter<>();

    @Override
    public Mono<CrapsGameDto> playAndBetCrapsGameByUser(String userUuid, BetDto betDto) {

        int expectedDiceSum = betDto.getExpectedDiceSum();
        int expectedAttempts = betDto.getExpectedAttempts();
        double amountBet = betDto.getAmountBet();

        return validateUuid(userUuid)
                .flatMap(uuid -> generateDiceRollsList(expectedDiceSum)
                        .flatMap(diceRollsList -> {
                            ResultCrapsGameDto resultDto = generateResultDto(expectedDiceSum, expectedAttempts, amountBet, diceRollsList);
                            return crapsGameRepository.save(buildCrapsGameDocument(userUuid, betDto, diceRollsList))
                                    .map(crapsGameDocument -> {
                                        CrapsGameDto crapsGameDto = crapsGameDocumentConverter.fromDocumentToDto(crapsGameDocument, CrapsGameDto.class);
                                        crapsGameDto.setResult(resultDto);
                                        return crapsGameDto;
                                    });
                        }))
                .doOnSuccess(dto -> log.info("Successfully played CrapsGame with Bet by user with ID: {}", userUuid))
                .doOnError(error -> log.error("Operation failed with error message: {}", error.getMessage()));
    }

    @Override
    public Mono<GenericResultDto<CrapsGameDto>> getCrapsGameByUser(String id, int offset, int limit) {
        return getCrapsGamesDtoByUserList(id)
                .flatMap(crapsGameList -> {
                    int total = crapsGameList.size();
                    List<CrapsGameDto> crapsGameListByPage = crapsGameList.stream()
                            .skip(offset)
                            .limit(limit == -1 ? total : limit)
                            .toList();
                    return Mono.just(new GenericResultDto<>(offset, limit, total, crapsGameListByPage.toArray(new CrapsGameDto[0])));
                });
    }

    @Override
    public Mono<UserCrapsGameStatsDto> getUserCrapsGameStats(String id) {
        return getCrapsGamesDtoByUserList(id)
                .flatMap(crapsGameDtoList -> {
                    UserCrapsGameStatsDto userCrapsGameStatsDto = generateUserCrapsGameStatsDto(crapsGameDtoList);
                    return Mono.just(userCrapsGameStatsDto);
                });
    }

    private Mono<List<CrapsGameDto>> getCrapsGamesDtoByUserList(String id) {
        return validateUuid(id)
                .flatMapMany(userId -> crapsGameRepository.findByUserId(userId)
                        .switchIfEmpty(Mono.error(new CrapsGameNotFoundException("No CrapsGames found for User with id " + userId))))
                .map(crapsGameDocument -> {
                    CrapsGameDto crapsGameDto = crapsGameDocumentConverter.fromDocumentToDto(crapsGameDocument, CrapsGameDto.class);
                    crapsGameDto.setResult(generateResultDto(crapsGameDocument.getExpectedDiceSum(), crapsGameDocument.getExpectedAttempts(),
                            crapsGameDocument.getAmountBet(), crapsGameDocument.getDiceRollsList()));
                    return crapsGameDto;
                })
                .collectList();
    }

    private Mono<List<DiceRollDocument>> generateDiceRollsList(int expectedDiceSum) {
        return Flux.range(0, 50)
                .map(i -> {
                    int dice1 = secureRandom.nextInt(6) + 1;
                    int dice2 = secureRandom.nextInt(6) + 1;
                    return new DiceRollDocument(dice1, dice2);
                })
                .takeUntil(diceRoll -> diceRoll.getDice1() + diceRoll.getDice2() == expectedDiceSum)
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

    private CrapsGameDocument buildCrapsGameDocument(String userUuid, BetDto
            betDto, List<DiceRollDocument> diceRollsList) {

        return CrapsGameDocument.builder()
                .uuid(UUID.randomUUID())
                .userId(UUID.fromString(userUuid))
                .date(LocalDateTime.now())
                .expectedDiceSum(betDto.getExpectedDiceSum())
                .expectedAttempts(betDto.getExpectedAttempts())
                .amountBet(betDto.getAmountBet())
                .diceRollsList(diceRollsList)
                .build();
    }

    private ResultCrapsGameDto generateResultDto(int expectedDiceSum, int expectedAttempts, double amountBet,
                                                 List<DiceRollDocument> diceRolls) {
        int attempts = diceRolls.size();
        boolean isWon = expectedAttempts >= attempts;
        double bettingOdds = calculateOdd(expectedDiceSum, expectedAttempts);
        double amountReturned = (amountBet * (isWon ? bettingOdds : -1));

        return ResultCrapsGameDto.builder()
                .attempts(attempts)
                .playerWins(isWon)
                .bettingOdds(bettingOdds)
                .amountReturned(amountReturned)
                .build();
    }

    private UserCrapsGameStatsDto generateUserCrapsGameStatsDto(List<CrapsGameDto> crapsGameDtoList) {

        int gamesPlayed = crapsGameDtoList.size();
        int gamesWon = (int) crapsGameDtoList.stream()
                .filter(crapsGame -> crapsGame.getResult().isPlayerWins())
                .count();
        double percentGamesWon = gamesWon * 100.0 / gamesPlayed;
        double totalAmountBet = crapsGameDtoList.stream()
                .mapToDouble(CrapsGameDto::getAmountBet)
                .sum();
        double totalAmountReturned = crapsGameDtoList.stream()
                .mapToDouble(crapsGame -> crapsGame.getResult().getAmountReturned())
                .sum();

        UserCrapsGameStatsDto.UserCrapsGameStatsDtoBuilder builder = UserCrapsGameStatsDto.builder();
        builder.userId(crapsGameDtoList.get(0).getUserId());
        builder.nameGame("Craps");
        builder.gamesPlayed(gamesPlayed);
        builder.gamesWon(gamesWon);
        builder.percentGamesWon(percentGamesWon);
        builder.totalAmountBet(totalAmountBet);
        builder.profitObtained(totalAmountReturned - totalAmountBet);
        return builder
                .build();

    }

    private Mono<UUID> validateUuid(String id) {
        boolean validUUID = !StringUtils.isEmpty(id) && UUID_FORM.matcher(id).matches();

        if (!validUUID) {
            log.warn("Invalid ID format.");
            return Mono.error(new BadUuidException("Invalid ID format. Please indicate the correct format."));
        }

        return Mono.just(UUID.fromString(id));
    }
}