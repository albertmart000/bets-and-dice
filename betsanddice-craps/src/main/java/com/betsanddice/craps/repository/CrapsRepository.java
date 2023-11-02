package com.betsanddice.craps.repository;

import com.betsanddice.craps.document.CrapsDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CrapsRepository extends ReactiveMongoRepository<CrapsDocument, UUID> {

    Mono<CrapsDocument> findByUuid(UUID uuid);
}
