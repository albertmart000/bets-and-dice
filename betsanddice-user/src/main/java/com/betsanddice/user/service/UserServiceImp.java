package com.betsanddice.user.service;

import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.exception.UserNotFoundException;
import com.betsanddice.user.helper.UserDocumentToDtoConverter;
import com.betsanddice.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Service
public class UserServiceImp implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImp.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDocumentToDtoConverter converter;

    @Override
    public Mono<UserDto> getUserByUuid(String uuid) {
        return Mono.just(UUID.fromString(uuid))
                .flatMap(userUuid -> userRepository.findByUuid(userUuid)
                        .map(user ->converter.fromDocumentToDto(user))
                        .switchIfEmpty(Mono.error(new UserNotFoundException("User with id " + userUuid + " not found")))
                        .doOnSuccess(userDto -> log.info("User found with ID: {}", userUuid))
                        .doOnError(error -> log.error("Error occurred while retrieving user: {}", error.getMessage()))
                );
    }

}

