package com.betsanddice.user.service;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.exception.BadUuidException;
import com.betsanddice.user.exception.UserNotFoundException;
import com.betsanddice.user.helper.UserDocumentToDtoConverter;
import com.betsanddice.user.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserServiceImp implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImp.class);
    private static final Pattern UUID_FORM = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    private final UserRepository userRepository;

    private final UserDocumentToDtoConverter converter;

    public UserServiceImp(UserRepository userRepository, UserDocumentToDtoConverter converter) {
        this.userRepository = userRepository;
        this.converter = converter;
    }

    public Flux<UserDto> getAllUsers(int offset, int limit) {
        Flux<UserDocument> usersList = userRepository.findAllByUuidNotNull()
                .skip(offset)
                .take(limit);
        return converter.fromDocumentFluxToDtoFlux(usersList);
    }

    @Override
    public Mono<UserDto> getUserByUuid(String uuid) {
        return validateUuid(uuid)
                .flatMap(userUuid -> userRepository.findByUuid(userUuid)
                        .switchIfEmpty(Mono.error(new UserNotFoundException("User with id " + userUuid + " not found")))
                        .map(user -> converter.fromDocumentToDto(user))
                        .doOnSuccess(userDto -> log.info("User found with ID: {}", userUuid))
                        .doOnError(error -> log.error("Error occurred while retrieving user: {}", error.getMessage()))
                );
    }

    Mono<UUID> validateUuid(String id) {
        boolean validUuid = !StringUtils.isEmpty(id) && UUID_FORM.matcher(id).matches();

        if (!validUuid) {
            log.warn("Invalid ID format: {}", id);
            return Mono.error(new BadUuidException("Invalid ID format. Please indicate the correct format."));
        }

        return Mono.just(UUID.fromString(id));
    }

}


