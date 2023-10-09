package com.betsanddice.user.repository;

import com.betsanddice.user.document.UserDocument;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

import java.util.UUID;

public interface IUserRepository extends ReactiveSortingRepository<UserDocument, UUID> {
}