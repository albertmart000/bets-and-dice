package com.betsanddice.tutorial.repository;

import com.betsanddice.tutorial.document.GameTutorialDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface GameTutorialRepository extends ReactiveMongoRepository<GameTutorialDocument, UUID> {

    Mono<GameTutorialDocument> findByUuid(UUID uuid);



}
