package com.betsanddice.craps.service;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.document.DiceRollDocument;
import com.betsanddice.craps.dto.BetDto;
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

    public Mono<CrapsGameDto> playCrapsGameByUser(String uuid) {
        return validateUuid(uuid)
                .flatMap(userUuid -> generateDiceRollsList()
                        .map(diceRollsList -> CrapsGameDocument.builder()
                                .uuid(UUID.randomUUID())
                                .userId(userUuid)
                                .date(LocalDateTime.now())
                                .diceRollsList(diceRollsList)
                                .build())
                        .flatMap(crapsGameDocument -> crapsGameRepository.save(crapsGameDocument))
                        .map(documentToSave -> converter.fromDocumentToDto(documentToSave, CrapsGameDto.class)))
                .doOnSuccess(crapsGameDocument -> log.info("Successfully played CrapsGame by user with ID: {}", uuid))
                .doOnError(error -> log.error("Operation failed with error message: {}", error.getMessage()));
    }

//    @Override
//    public Mono<CrapsGameDto> playAndBetCrapsGameByUser(String uuid, double amountBet, int playerResult) {
//        return validateUuid(uuid)
//                .flatMap(userUuid -> generateDiceRollsList()
//                        .flatMap(diceRollsList -> {
//                            boolean isWin = playerResult >= diceRollsList.size();
//                            double betOdd = calculateBetOdds(playerResult);
//                            BigDecimal amountWon = calculateAmountWon(amountBet, isWin, betOdd);
//                            //BigDecimal amountWon = BigDecimal.valueOf(amountWagered * (isWon ? betCoefficient : -1));
//
//                            BetDocument betDocument = buildBetDocument(playerResult, amountBet);
//                            Mono<BetDto> betDto = buildBetDto(playerResult, amountBet, isWin, amountWon);
//                            Mono<CrapsGameDocument> crapsGameDocument = buildCrapsGameDocument(String.valueOf(userUuid), betDocument, diceRollsList);
//                        };
//
//
//        return crapsGameRepository.save(crapsGameDocument)
//                .map(savedDocument -> {
//                    CrapsGameDto crapsGameDto = converter.fromDocumentToDto(savedDocument, CrapsGameDto.class);
//                    crapsGameDto.setBetDto(betDto);
//                    return crapsGameDto;
//                });
//
//
//
//        .doOnSuccess(crapsGameDto -> log.info("Successfully played CrapsGame with Bet by user with ID: {}", uuid))
//                .doOnError(error -> log.error("Operation failed with error message: {}", error.getMessage()));
//    }
//
//    private double calculateBetOdds(int playerResult) {
//        return switch (playerResult) {
//            case 1 -> 2.0;
//            case 3 -> 1.5;
//            case 5 -> 1.25;
//            default -> 0;
//        };
//    }
//
//    private Mono<List<DiceRollDocument>> generateDiceRollsList() {
//        return Flux.<DiceRollDocument>generate(flux -> {
//                    int dice1 = secureRandom.nextInt(6) + 1;
//                    int dice2 = secureRandom.nextInt(6) + 1;
//                    int result = dice1 + dice2;
//                    flux.next(new DiceRollDocument(dice1, dice2));
//                    if (result == 7) {
//                        flux.complete();
//                    }
//                })
//                .collectList();
//    }
//
//    private BigDecimal calculateAmountWon(double amountBet, boolean isWin, double betOdd) {
//        return BigDecimal.valueOf(amountBet * (isWin ? betOdd : -1));
//    }
//
//    private Mono<BetDto> buildBetDto(int playerResult, double amountBet, boolean isWin, BigDecimal amountWon) {
//        return Mono.just(BetDto.builder()
//                .playerResult(playerResult)
//                .amountBet(BigDecimal.valueOf(amountBet))
//                .playerWins(isWin)
//                .amountWon(amountWon)
//                .build());
//    }
//
//    private BetDocument buildBetDocument(int playerResult, double amountBet) {
//        return  BetDocument.builder()
//                .playerResult(playerResult)
//                .amountBet(BigDecimal.valueOf(amountBet))
//                .build();
//    }
//
//    private Mono <CrapsGameDocument> buildCrapsGameDocument(String userUuid, BetDocument betDocument,
//                                                     List<DiceRollDocument> diceRollsList ) {
//        return Mono.just(CrapsGameDocument.builder()
//                .uuid(UUID.randomUUID())
//                .userId(UUID.fromString(userUuid))
//                .date(LocalDateTime.now())
//                .betDocument(betDocument)
//                .diceRollsList(diceRollsList)
//                .build());
//    }
//
//    private Mono<CrapsGameDto> saveAndConvertCrapsGame(CrapsGameDocument crapsGameDocument, BetDto betDto) {
//        return crapsGameRepository.save(crapsGameDocument)
//                .map(savedDocument -> {
//                    CrapsGameDto crapsGameDto = converter.fromDocumentToDto(savedDocument, CrapsGameDto.class);
//                    crapsGameDto.setBetDto(betDto);
//                    return crapsGameDto;
//                });
//    }
    
    @Override
    public Mono<CrapsGameDto> playAndBetCrapsGameByUser(String uuid, double amountBet, int playerResult) {
        return validateUuid(uuid)
                .flatMap(userUuid -> generateDiceRollsList()
                        .flatMap(diceRollsList -> {
                            boolean isWon = playerResult >= diceRollsList.size();
                            double betCoefficient = switch (playerResult) {
                                case 1 -> 2.0;
                                case 3 -> 1.5;
                                case 5 -> 1.25;
                                default -> 0;
                            };

                            BigDecimal amountWon = BigDecimal.valueOf(amountBet * (isWon ? betCoefficient : -1));

                            BetDto betDto = BetDto.builder()
                                    .playerResult(playerResult)
                                    .amountBet(BigDecimal.valueOf(amountBet))
                                    .playerWins(isWon)
                                    .amountWon(amountWon)
                                    .build();

                            CrapsGameDocument crapsGameDocument = CrapsGameDocument.builder()
                                    .uuid(UUID.randomUUID())
                                    .userId(userUuid)
                                    .date(LocalDateTime.now())
                                    .diceRollsList(diceRollsList)
                                    .build();

                            return crapsGameRepository.save(crapsGameDocument)
                                    .map(savedDocument -> {
                                        CrapsGameDto crapsGameDto = converter.fromDocumentToDto(savedDocument, CrapsGameDto.class);
                                        crapsGameDto.setBetDto(betDto);
                                        return crapsGameDto;
                                    });
                        }))
                .doOnSuccess(crapsGameDto -> log.info("Successfully played CrapsGame with Bet by user with ID: {}", uuid))
                .doOnError(error -> log.error("Operation failed with error message: {}", error.getMessage()));
    }


    private Mono<List<DiceRollDocument>> generateDiceRollsList() {
        return Flux.<DiceRollDocument>generate(flux -> {
                    int dice1 = secureRandom.nextInt(6) + 1;
                    int dice2 = secureRandom.nextInt(6) + 1;
                    int result = dice1 + dice2;
                    flux.next(new DiceRollDocument(dice1, dice2));
                    if (result == 7) {
                        flux.complete();
                    }
                })
                .collectList();
    }

//    private Mono<List<DiceRollDto>> generateDiceRollsList() {
//        return Flux.<DiceRollDto>generate(flux -> {
//                    int dice1 = secureRandom.nextInt(6) + 1;
//                    int dice2 = secureRandom.nextInt(6) + 1;
//                    int result = dice1 + dice2;
//                    flux.next(new DiceRollDto(dice1, dice2, result));
//                    if (result == 7) {
//                        flux.complete();
//                    }
//                })
//                .collectList();
//    }

    private Mono<UUID> validateUuid(String id) {
        boolean validUuid = !StringUtils.isEmpty(id) && UUID_FORM.matcher(id).matches();

        if (!validUuid) {
            log.warn("Invalid ID format: {}", id);
            return Mono.error(new BadUuidException("Invalid ID format. Please indicate the correct format."));
        }

        return Mono.just(UUID.fromString(id));
    }

}