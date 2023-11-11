package com.betsanddice.stat.repository;

import com.betsanddice.stat.document.UserStatDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface UserStatRepository extends ReactiveMongoRepository<UserStatDocument, UUID> {

    Flux<UserStatDocument> findAll();

}
