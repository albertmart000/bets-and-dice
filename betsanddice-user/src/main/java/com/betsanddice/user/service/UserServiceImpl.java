package com.betsanddice.user.service;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.dto.GenericResultDto;
import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.dto.UserRegisterDto;
import com.betsanddice.user.exception.UserAlreadyExistException;
import com.betsanddice.user.exception.UserNotFoundException;
import com.betsanddice.user.helper.DocumentToDtoConverter;
import com.betsanddice.user.repository.UserRepository;
import com.betsanddice.user.utils.StringToUuidValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentToDtoConverter<UserDocument, UserDto> converter = new DocumentToDtoConverter<>();

    @Autowired
    private StringToUuidValidator uuidValidator;

    @Override
    public Mono<UserDto> registerUser(UserRegisterDto userRegisterDto) {
        return userRepository.findByEmail(userRegisterDto.getEmail())
                .flatMap(userByEmail ->
                        Mono.<UserDto>error(new UserAlreadyExistException("User with email " +
                                userRegisterDto.getEmail() + " already exists")))
                .doOnError(e -> log.error("Error occurred while registering user: {}", e.getMessage()))
                .switchIfEmpty(userRepository.findByNickname(userRegisterDto.getNickname())
                        .flatMap(userByNickname ->
                                Mono.<UserDto>error(new UserAlreadyExistException("User with nickname " +
                                        userRegisterDto.getNickname() + " already exists")))
                        .doOnError(e -> log.error("Error occurred while registering user: {}", e.getMessage()))
                        .switchIfEmpty(userRepository.save(buildUserDocument(userRegisterDto))
                                .map(savedUser -> converter.fromDocumentToDto(savedUser, UserDto.class)))
                        .doOnSuccess(userDto -> log.info("User registered successfully with email: {}", userRegisterDto.getEmail()))
                );
    }

    @Override
    public Mono<UserDto> getUserById(String id) {
        return uuidValidator.validateUuid(id)
                .flatMap(userId -> userRepository.findByUuid(userId)
                        .switchIfEmpty(Mono.error(new UserNotFoundException("User with id " + userId + " not found")))
                        .map(user -> converter.fromDocumentToDto(user, UserDto.class))
                        .doOnSuccess(userDto -> log.info("User found with ID: {}", userId))
                        .doOnError(error -> log.error("Error occurred while retrieving user: {}", error.getMessage()))
                );
    }

    @Override
    public Mono<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User with email" + email+ " not found")))
                .map(user -> converter.fromDocumentToDto(user, UserDto.class))
                .doOnSuccess(userDto -> log.info("User found with email: {}", email))
                .doOnError(error -> log.error("Error occurred while retrieving user: {}", error.getMessage()));
    }

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

    private UserDocument buildUserDocument(UserRegisterDto userRegisterDto) {
        return UserDocument.builder()
                .uuid(UUID.randomUUID())
                .firstName(userRegisterDto.getName())
                .surname(userRegisterDto.getSurname())
                .birthdate(userRegisterDto.getBirthdate())
                .nickname(userRegisterDto.getNickname())
                .email(userRegisterDto.getEmail())
                .password(userRegisterDto.getPassword())
                .registrationDate(LocalDateTime.now())
                .build();
    }

}