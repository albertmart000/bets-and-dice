package com.betsanddice.user.service;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.exception.BadUuidException;
import com.betsanddice.user.exception.UserNotFoundException;
import com.betsanddice.user.helper.UserDocumentToDtoConverter;
import com.betsanddice.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImpTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDocumentToDtoConverter converter;

    @InjectMocks
    private UserServiceImp userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserById_ValidId_UserFound() {
        UUID userId = UUID.randomUUID();
        UserDocument userDocument = new UserDocument();
        UserDto userDto = new UserDto();

        when(userRepository.findByUuid(userId)).thenReturn(Mono.just(userDocument));
        when(converter.fromDocumentToDto(userDocument)).thenReturn(userDto);

        Mono<UserDto> resultDto = userService.getUserByUuid(userId.toString());

        StepVerifier.create(resultDto)
                .expectNext(userDto)
                .expectComplete()
                .verify();

        verify(userRepository).findByUuid(userId);
        verify(converter).fromDocumentToDto(userDocument);
    }

    @Test
    void getUserById_NonExistId_ErrorThrown() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findByUuid(userId)).thenReturn(Mono.empty());

        Mono<UserDto> resultDto = userService.getUserByUuid(userId.toString());

        StepVerifier.create(resultDto)
                .expectError(UserNotFoundException.class)
                .verify();

        verify(userRepository).findByUuid(userId);
        verifyNoInteractions(converter);
    }

    @Test
    void getAllChallenges_ChallengesExist_ChallengesReturned() {

        UserDocument userDocument1 = new UserDocument();
        userDocument1.setUuid(UUID.randomUUID());
        UserDocument userDocument2 = new UserDocument();
        userDocument2.setUuid(UUID.randomUUID());
        UserDocument userDocument3 = new UserDocument();
        userDocument3.setUuid(UUID.randomUUID());
        UserDocument userDocument4 = new UserDocument();
        userDocument4.setUuid(UUID.randomUUID());

        UserDto userDto1 = new UserDto();
        UserDto userDto2 = new UserDto();
        UserDto userDto3 = new UserDto();
        UserDto userDto4 = new UserDto();
        UserDto[] expectedUsers = {userDto1, userDto2, userDto3, userDto4};

        when(userRepository.findAll()).thenReturn(Flux.just(new UserDocument(), new UserDocument()));
        when(converter.fromDocumentFluxToDtoFlux(any())).thenReturn(Flux.just(userDto1, userDto2));

        int offset = 1;
        int limit = 2;

        when(userRepository.findAllByUuidNotNull()).thenReturn(Flux.just(userDocument1, userDocument2, userDocument3, userDocument4));
        when(converter.fromDocumentFluxToDtoFlux(any())).thenReturn(Flux.just(userDto1, userDto2, userDto3, userDto4));

        Flux<UserDto> result = userService.getAllUsers(offset, limit);
        verify(userRepository).findAllByUuidNotNull();
        verify(converter).fromDocumentFluxToDtoFlux(any());

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextCount(4)
                .expectComplete()
                .verify();

        StepVerifier.create(result.skip(offset).take(limit))
                .expectSubscription()
                .expectNext( userDto2, userDto3)
                .expectComplete()
                .verify();

        StepVerifier.create(userRepository.findAllByUuidNotNull().skip(offset).take(limit))
                .expectSubscription()
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    void validateUuid_ValidUuid_ReturnsMonoWithUuid_Test() {
        String validUuid = "550e8400-e29b-41d4-a716-446655440000";

        Mono<UUID> resultMono = userService.validateUuid(validUuid);

        StepVerifier.create(resultMono)
                .expectNextMatches(uuid -> uuid.toString().equals(validUuid))
                .expectComplete()
                .verify();
    }

    @Test
    void validateUuid_InvalidUuid_ReturnsErrorMono_Test() {
        String invalidUuid = "invalid-uuid";

        Mono<UUID> resultMono = userService.validateUuid(invalidUuid);

        StepVerifier.create(resultMono)
                .expectError(BadUuidException.class)
                .verify();
    }

    @Test
    void validateUuid_EmptyUuid_ReturnsErrorMono_Test() {
        String emptyUuid = "";

        Mono<UUID> resultMono = userService.validateUuid(emptyUuid);

        StepVerifier.create(resultMono)
                .expectError(BadUuidException.class)
                .verify();
    }

    @Test
    void validateUuid_NullUuid_ReturnsErrorMono_Test() {
        Mono<UUID> resultMono = userService.validateUuid(null);

        StepVerifier.create(resultMono)
                .expectError(BadUuidException.class)
                .verify();
    }
}