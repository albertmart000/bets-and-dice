package com.betsanddice.game.clients;

import com.betsanddice.tutorial.dto.GameTutorialDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

public interface ITutorialClient {

    @GetMapping(path = "/gameTutorials/game/{gameUuid}")
    Mono<GameTutorialDto> getGameTutorialByGameUuid(@PathVariable("gameUuid") String uuid);

    }
