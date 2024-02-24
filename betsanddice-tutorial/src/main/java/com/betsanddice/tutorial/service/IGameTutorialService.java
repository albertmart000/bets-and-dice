package com.betsanddice.tutorial.service;

import com.betsanddice.tutorial.document.GameTutorialDocument;
import com.betsanddice.tutorial.dto.GameTutorialDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IGameTutorialService {

    Mono<GameTutorialDto> addGameTutorial(GameTutorialDto gameTutorialDto);

    Flux<GameTutorialDto> getAllGameTutorials();

    Mono<GameTutorialDto> getGameTutorialByUuid(String uuid);

    Mono<GameTutorialDto> getGameTutorialByGameId(String gameId);
}
