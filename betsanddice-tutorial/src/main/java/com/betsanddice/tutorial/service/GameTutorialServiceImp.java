package com.betsanddice.tutorial.service;

import com.betsanddice.tutorial.document.GameTutorialDocument;
import com.betsanddice.tutorial.dto.GameTutorialDto;
import com.betsanddice.tutorial.exception.BadUuidException;
import com.betsanddice.tutorial.exception.GameTutorialAlreadyExistException;
import com.betsanddice.tutorial.exception.GameTutorialNotFoundException;
import com.betsanddice.tutorial.helper.GameTutorialDocumentToDtoConverter;
import com.betsanddice.tutorial.repository.GameTutorialRepository;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class GameTutorialServiceImp implements IGameTutorialService {

    private static final Logger log = LoggerFactory.getLogger(GameTutorialServiceImp.class);
    private static final Pattern UUID_FORM = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    private final GameTutorialRepository gameTutorialRepository;

    private final GameTutorialDocumentToDtoConverter documentToDtoConverter;

    public GameTutorialServiceImp(GameTutorialRepository gameTutorialRepository, GameTutorialDocumentToDtoConverter documentToDtoConverter) {
        this.gameTutorialRepository = gameTutorialRepository;
        this.documentToDtoConverter = documentToDtoConverter;
    }

    public Mono<GameTutorialDto> addGameTutorial(GameTutorialDto gameTutorialDto) {
        return Mono.just(gameTutorialDto)
                .flatMap(gameName -> {
                    Mono<GameTutorialDocument> gameTutorialDocumentMono = gameTutorialRepository.findByGameName(gameTutorialDto.getGameName());
                    return gameTutorialDocumentMono
                            .hasElement()
                            .flatMap(result -> {
                                if (Boolean.FALSE.equals(result)) {
                                    GameTutorialDocument gameTutorialDocument = new GameTutorialDocument();
                                    BeanUtils.copyProperties(gameTutorialDto, gameTutorialDocument);
                                    gameTutorialDocument.setUuid(UUID.randomUUID());
                                    return Mono.just(gameTutorialDocument)
                                            .flatMap(gameTutorialRepository::save)
                                            .map(documentToDtoConverter::fromDocumentToDto);
                                } else {
                                    return Mono.error(new GameTutorialAlreadyExistException("Game with name " + gameTutorialDto.getGameName() + " already exist."));
                                }
                            })
                            .doOnSuccess(gameTutorialDtoAdded -> log.info("Game Tutorial saved with name: {}", gameName))
                            .doOnError(error -> log.error("Error occurred while saving Game Tutorial: {}", error.getMessage()));
                });
    }

    @Override
    public Mono<GameTutorialDto> getGameTutorialByUuid(String uuid) {
        return validateUuid(uuid)
                .flatMap(gameTutorialUuid -> gameTutorialRepository.findByUuid(gameTutorialUuid)
                        .switchIfEmpty(Mono.error(new GameTutorialNotFoundException("GameTutorial with id " + gameTutorialUuid + " not found")))
                        .map(documentToDtoConverter::fromDocumentToDto)
                        .doOnSuccess(gameTutorialDto -> log.info("GameTutorial found with ID: {}", gameTutorialUuid))
                        .doOnError(error -> log.error("Error occurred while retrieving game: {}", error.getMessage()))
                );
    }

    @Override
    public Mono<GameTutorialDto> getGameTutorialByGameId(String gameId) {
        return validateUuid(gameId)
                .flatMap(gameUuid -> gameTutorialRepository.findByGameId(gameUuid)
                        .switchIfEmpty(Mono.error(new GameTutorialNotFoundException("GameTutorial with id " + gameUuid + " not found")))
                        .map(documentToDtoConverter::fromDocumentToDto)
                        .doOnSuccess(gameTutorialDto -> log.info("GameTutorial found with ID: {}", gameUuid))
                        .doOnError(error -> log.error("Error occurred while retrieving game: {}", error.getMessage()))
                );
    }

    @Override
    public Flux<GameTutorialDto> getAllGameTutorials(int offset, int limit)  {
        Flux<GameTutorialDocument> gameTutorialsList = gameTutorialRepository.findAllByUuidNotNull()
                .skip(offset)
                .take(limit);
        return documentToDtoConverter.fromDocumentFluxToDtoFlux(gameTutorialsList);
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
