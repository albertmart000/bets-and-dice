package com.betsanddice.user.service;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.dto.GenericResultDto;
import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.exception.BadUuidException;
import com.betsanddice.user.exception.UserNotFoundException;
import com.betsanddice.user.helper.DocumentToDtoConverter;
import com.betsanddice.user.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserServiceImp implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImp.class);
    private static final Pattern UUID_FORM = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentToDtoConverter<UserDocument, UserDto> converter = new DocumentToDtoConverter<>();

    @Override
    public Mono<GenericResultDto<UserDto>> getAllUsers(int offset, int limit) {

        Mono<Long> countUsers = userRepository.count();
        Flux<UserDto> userDtoFlux = converter.fromDocumentFluxToDtoFlux(
                userRepository.findAllByUuidNotNullExcludingTestingValues()
                        .skip(offset)
                        .take(limit),
                UserDto.class);

        return countUsers.zipWith(userDtoFlux.collectList(), (totalCount, users) -> {

            UserDto[] userArray = users.toArray(new UserDto[0]);
            return new GenericResultDto<>(offset, limit, totalCount.intValue(), userArray);
        }).onErrorResume(e -> Mono.just(new GenericResultDto<>(offset, limit, 0, new UserDto[0])));
    }

    @Override
    public Mono<UserDto> getUserById(String id) {
        return validateUuid(id)
                .flatMap(userId -> userRepository.findByUuid(userId)
                        .switchIfEmpty(Mono.error(new UserNotFoundException("User with id " + userId + " not found")))
                        .map(user -> converter.fromDocumentToDto(user, UserDto.class))
                        .doOnSuccess(userDto -> log.info("User found with ID: {}", userId))
                        .doOnError(error -> log.error("Error occurred while retrieving user: {}", error.getMessage()))
                );
    }

    protected Mono<UUID> validateUuid(String id) {
        boolean validUuid = !StringUtils.isEmpty(id) && UUID_FORM.matcher(id).matches();

        if (!validUuid) {
            log.warn("Invalid ID format: {}", id);
            return Mono.error(new BadUuidException("Invalid ID format. Please indicate the correct format."));
        }

        return Mono.just(UUID.fromString(id));
    }

}


