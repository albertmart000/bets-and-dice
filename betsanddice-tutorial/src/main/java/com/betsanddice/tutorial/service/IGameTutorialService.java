package com.betsanddice.tutorial.service;

import com.betsanddice.tutorial.dto.GameTutorialDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IGameTutorialService {

    Mono<GameTutorialDto> addGameTutorial(GameTutorialDto gameTutorialDto);

    Mono<GameTutorialDto> getGameTutorialByUuid(String uuid);

    Mono<GameTutorialDto> getGameTutorialByGameId(String gameId);

    Flux<GameTutorialDto> getAllGameTutorials();
}
