package com.betsanddice.game.service;

import com.betsanddice.game.document.GameDocument;
import com.betsanddice.game.dto.GameDto;
import com.betsanddice.game.exception.BadUuidException;
import com.betsanddice.game.exception.GameNotFoundException;
import com.betsanddice.game.helper.GameDocumentToDtoConverter;
import com.betsanddice.game.repository.GameRepository;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class GameServiceImp implements IGameService {

    private static final Logger log = LoggerFactory.getLogger(GameServiceImp.class);
    private static final Pattern UUID_FORM = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);


    private GameRepository gameRepository;

    private GameDocumentToDtoConverter documentToDtoConverter;

    public GameServiceImp(GameRepository gameRepository, GameDocumentToDtoConverter documentToDtoConverter) {
        this.gameRepository = gameRepository;
        this.documentToDtoConverter = documentToDtoConverter;
    }

    @Override
    public Mono<GameDto> getGameByUuid(String uuid) {
        return validateUuid(uuid)
                .flatMap(gameUuid -> gameRepository.findByUuid(gameUuid)
                        .switchIfEmpty(Mono.error(new GameNotFoundException("Game with id " + gameUuid + " not found")))
                        .map(gameDocument -> documentToDtoConverter.fromDocumentToDto(gameDocument))
                        .doOnSuccess(gameDto -> log.info("Game found with ID: {}", gameUuid))
                        .doOnError(error -> log.error("Error occurred while retrieving game: {}", error.getMessage()))
                );
    }

    @Override
    public Flux<GameDto> getAllGames() {
        Flux<GameDocument> gameList = gameRepository.findAll();
        return documentToDtoConverter.fromDocumentFluxToDtoFlux(gameList);
    }

    Mono<UUID> validateUuid(String id) {
        boolean validUuid = !StringUtils.isEmpty(id) && UUID_FORM.matcher(id).matches();

        if (!validUuid) {
            log.warn("Invalid ID format: {}", id);
            return Mono.error(new BadUuidException("Invalid ID format. Please indicate the correct format."));
        }
        return Mono.just(UUID.fromString(id));
    }
}
