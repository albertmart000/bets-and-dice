package com.betsanddice.user.repository;

import com.betsanddice.user.document.UserDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;
@Repository
public interface UserRepository extends ReactiveMongoRepository<UserDocument, UUID> {

    Mono<Boolean> existsByUuid(UUID uuid);

    Mono<UserDocument> findByUuid(UUID uuid);
}