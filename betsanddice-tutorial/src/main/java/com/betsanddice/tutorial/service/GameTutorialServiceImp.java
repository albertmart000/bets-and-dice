package com.betsanddice.tutorial.service;

import com.betsanddice.tutorial.document.GameTutorialDocument;
import com.betsanddice.tutorial.dto.in.GameTutorialDtoByName;
import com.betsanddice.tutorial.dto.out.GameTutorialDto;
import com.betsanddice.tutorial.exception.BadUuidException;
import com.betsanddice.tutorial.exception.GameTutorialAlreadyExistException;
import com.betsanddice.tutorial.exception.GameTutorialNotFoundException;
import com.betsanddice.tutorial.helper.GameTutorialDocumentToDtoConverter;
import com.betsanddice.tutorial.helper.GameTutorialDtoToDocumentConverter;
import com.betsanddice.tutorial.repository.GameTutorialRepository;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class GameTutorialServiceImp implements IGameTutorialService {

    private static final Logger log = LoggerFactory.getLogger(GameTutorialServiceImp.class);
    private static final Pattern UUID_FORM = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    @Autowired
    private GameTutorialRepository gameTutorialRepository;

    @Autowired
    private GameTutorialDocumentToDtoConverter documentToDtoConverter;

    @Autowired
    private GameTutorialDtoToDocumentConverter dtoToDocumentConverter;

    @Override
    public Mono<GameTutorialDto> addGameTutorial(GameTutorialDtoByName gameTutorialDtoByName) {
        return Mono.just(gameTutorialDtoByName.getGameName())
                .flatMap(gameNameTutorialToAdd -> {
                    Mono<Boolean> existGameTutorial = gameTutorialRepository.existsByGameName(gameNameTutorialToAdd);
                    return existGameTutorial
                            .flatMap(result -> {
                                if (Boolean.TRUE.equals(result)) {
                                    return Mono.error(new GameTutorialAlreadyExistException("Game with name " + gameTutorialDtoByName + " already exist."));
                                } else {
                                    GameTutorialDocument gameTutorialDocumentToAdd = dtoToDocumentConverter.fromDtoToDocument(gameTutorialDtoByName);
                                    gameTutorialDocumentToAdd.setUuid(UUID.randomUUID());
                                    gameTutorialRepository.save(gameTutorialDocumentToAdd);
                                    return Mono.just(documentToDtoConverter.fromDocumentToDto(gameTutorialDocumentToAdd));
                                }
                            })
                            .doOnSuccess(gameTutorialDto -> log.info("Game Tutorial saved with ID: {}", gameTutorialDto.getGameId()))
                            .doOnError(error -> log.error("Error occurred while saving Game Tutorial: {}", error.getMessage()));
                });
    }

    /*if (Boolean.FALSE.equals(result)) */
/*    @Override
    public Mono<GameTutorialDto> addGameTutorial(GameTutorialDtoByName gameTutorialDtoByName) {
        GameTutorialDocument gameTutorialDocument = new GameTutorialDocument(UUID.randomUUID(),
                gameTutorialDtoByName.getGameName(), gameTutorialDtoByName.getRules());
        gameTutorialRepository.save(gameTutorialDocument).block();
        GameTutorialDto gameTutorialDto = converter.fromDocumentToDto(gameTutorialDocument);
        return Mono.just(gameTutorialDto);
    }*/

    @Override
    public Flux<GameTutorialDto> getAllGameTutorials() {
        Flux<GameTutorialDocument> gameTutorialsList = gameTutorialRepository.findAll();
        return documentToDtoConverter.fromDocumentFluxToDtoFlux(gameTutorialsList);
    }

    @Override
    public Mono<GameTutorialDto> getGameTutorialByUuid(String uuid) {
        return validateUuid(uuid)
                .flatMap(gameTutorialUuid -> gameTutorialRepository.findByUuid(gameTutorialUuid)
                        .map(gameTutorialDocument -> documentToDtoConverter.fromDocumentToDto(gameTutorialDocument))
                        .switchIfEmpty(Mono.error(new GameTutorialNotFoundException("GameTutorial with id " + gameTutorialUuid + " not found")))
                        .doOnSuccess(gameTutorialDto -> log.info("GameTutorial found with ID: {}", gameTutorialUuid))
                        .doOnError(error -> log.error("Error occurred while retrieving game: {}", error.getMessage()))
                );
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
