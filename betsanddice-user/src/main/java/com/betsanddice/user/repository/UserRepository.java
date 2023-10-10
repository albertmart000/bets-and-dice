package com.betsanddice.user.repository;

import com.betsanddice.user.document.UserDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface UserRepository extends ReactiveMongoRepository<UserDocument, UUID> {
}