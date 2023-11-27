package com.betsanddice.tutorial.service;

import com.betsanddice.tutorial.document.GameTutorialDocument;
import com.betsanddice.tutorial.dto.GameTutorialDto;
import com.betsanddice.tutorial.exception.BadUuidException;
import com.betsanddice.tutorial.exception.GameTutorialAlreadyExistException;
import com.betsanddice.tutorial.exception.GameTutorialNotFoundException;
import com.betsanddice.tutorial.helper.GameTutorialDocumentToDtoConverter;
import com.betsanddice.tutorial.helper.GameTutorialDtoToDocumentConverter;
import com.betsanddice.tutorial.repository.GameTutorialRepository;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
                                            .flatMap(gameTutorialDocumentToSave -> gameTutorialRepository.save(gameTutorialDocumentToSave))
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
