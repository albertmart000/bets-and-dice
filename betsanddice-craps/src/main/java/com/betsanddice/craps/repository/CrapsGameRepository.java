package com.betsanddice.craps.repository;

import com.betsanddice.craps.document.CrapsGameDocument;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface CrapsGameRepository extends ReactiveMongoRepository<CrapsGameDocument, UUID> {

    @Query(value = "{ 'userId' : ?0 }")
    Flux<CrapsGameDocument> findByUserId(UUID userId);

}