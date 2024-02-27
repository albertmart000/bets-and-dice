package com.betsanddice.game.repository;

import com.betsanddice.game.document.CrapsGameDocument;
import com.betsanddice.game.document.GameDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface GameRepository extends ReactiveMongoRepository<GameDocument, UUID> {

    Flux<GameDocument> findAll();
}
