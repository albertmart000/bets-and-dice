package com.betsanddice.stat.repository;

import com.betsanddice.stat.document.UserGameStatDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface UserGameStatRepository extends ReactiveMongoRepository<UserGameStatDocument, UUID> {

    Flux<UserGameStatDocument> findAll();

}
