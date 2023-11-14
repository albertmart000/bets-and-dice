package com.betsanddice.craps.service;

import com.betsanddice.craps.document.CrapsGameDocument;
import com.betsanddice.craps.dto.CrapsGameDto;
import com.betsanddice.craps.exception.BadUuidException;
import com.betsanddice.craps.helper.CrapsGameDocumentToDtoConverter;
import com.betsanddice.craps.repository.CrapsGameRepository;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class CrapsGameServiceImp implements ICrapsGameService{

    private static final Logger log = LoggerFactory.getLogger(CrapsGameServiceImp.class);
    private static final Pattern UUID_FORM = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    @Autowired
    private CrapsGameRepository crapsGameRepository;

    @Autowired
    private CrapsGameDocumentToDtoConverter crapsGameConverter = new CrapsGameDocumentToDtoConverter();

    @Override
    public Mono<CrapsGameDto> addCrapsGame() {
        return null;
    }

/*    @Override
    public GameDTO addGame(GameDTO gameDTO, Long playerId) {
        GameDTO newGameDTO = createGame(playerId);
        Game newGame = mapDTOToEntity(newGameDTO);
        gameRepository.save(newGame);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Jugador", "id", playerId));
        List<Game> listGamesPlayer = player.getGameList();
        player.setGameList(listGamesPlayer);
        double rateGamesWon;
        double gamesWon = 0;
        for (Game gamePlayed : listGamesPlayer) {
            if (gamePlayed.getDice1() + gamePlayed.getDice2() == 7)
                gamesWon++;
        }
        rateGamesWon = (gamesWon / listGamesPlayer.size()) * 100;
        player.setRateGamesWon(rateGamesWon);
        playerRepository.save(player);
        return mapEntityToDTO(newGame);

    public Mono<GenericResultDto<SolutionDto>> getSolutions(String idChallenge, String idLanguage) {
        Mono<UUID> challengeIdMono = validateUUID(idChallenge);
        Mono<UUID> languageIdMono = validateUUID(idLanguage);

        return Mono.zip(challengeIdMono, languageIdMono)
                .flatMap(tuple -> {
                    UUID challengeId = tuple.getT1();
                    UUID languageId = tuple.getT2();

                    return challengeRepository.findByUuid(challengeId)
                            .switchIfEmpty(Mono.error(new ChallengeNotFoundException(String.format(CHALLENGE_NOT_FOUND_ERROR, challengeId))))
                            .flatMapMany(challenge -> Flux.fromIterable(challenge.getSolutions())
                                    .flatMap(solutionId -> solutionRepository.findById(solutionId))
                                    .filter(solution -> solution.getIdLanguage().equals(languageId))
                                    .flatMap(solution -> Mono.from(solutionConverter.convertDocumentFluxToDtoFlux(Flux.just(solution), SolutionDto.class)))
                            )
                            .collectList()
                            .map(solutions -> {
                                GenericResultDto<SolutionDto> resultDto = new GenericResultDto<>();
                                resultDto.setInfo(0, solutions.size(), solutions.size(), solutions.toArray(new SolutionDto[0]));
                                return resultDto;
                            });
                });
    }
    }
    }*/

    @Override
    public Flux<CrapsGameDto> getAllCrapsGame() {
        Flux<CrapsGameDocument> crapsGameList = crapsGameRepository.findAll();
        return crapsGameConverter.fromDocumentFluxToDtoFlux(crapsGameList);
    }

    private Mono<UUID> validateUUID(String id) {
        boolean validUUID = !StringUtils.isEmpty(id) && UUID_FORM.matcher(id).matches();

        if (!validUUID) {
            log.warn("Invalid ID format: {}", id);
            return Mono.error(new BadUuidException("Invalid ID format. Please indicate the correct format."));
        }

        return Mono.just(UUID.fromString(id));
    }

}
