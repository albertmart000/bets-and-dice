package com.betsanddice.game.repository;

import com.betsanddice.game.document.GameDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface GameRepository extends ReactiveMongoRepository<GameDocument, UUID> {

    Mono<Boolean> existsByUuid(UUID uuid);

    Mono<GameDocument> findByUuid(UUID uuid);

    Flux<GameDocument> findAll();
}
