package com.betsanddice.game.repository;

import com.betsanddice.game.document.CrapsGameDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CrapsGameRepository extends ReactiveMongoRepository<CrapsGameDocument, UUID> {

    Flux<CrapsGameDocument> findAll();

    Mono<CrapsGameDocument> save(CrapsGameDocument crapsGameDocument);

}
