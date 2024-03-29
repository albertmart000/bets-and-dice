package com.betsanddice.tutorial.repository;

import com.betsanddice.tutorial.document.GameTutorialDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface GameTutorialRepository extends ReactiveMongoRepository<GameTutorialDocument, UUID> {

    Mono<Boolean> existsByUuid(UUID uuid);

    Mono<Boolean> existsByGameId(UUID gameId);

    Mono<Boolean> existsByGameName(String gameName);

    Mono<GameTutorialDocument> findByUuid(UUID uuid);

    Mono<GameTutorialDocument> findByGameId(UUID gameId);

    Mono<GameTutorialDocument> findByGameName(String gameName);

    Mono<GameTutorialDocument> save(GameTutorialDocument gameTutorialDocument);

    Flux<GameTutorialDocument> findAllByUuidNotNull();
}
