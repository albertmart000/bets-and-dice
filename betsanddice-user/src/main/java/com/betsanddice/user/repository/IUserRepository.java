package com.betsanddice.user.repository;

import com.betsanddice.user.document.UserDocument;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface IUserRepository extends ReactiveSortingRepository<UserDocument, UUID> {
}