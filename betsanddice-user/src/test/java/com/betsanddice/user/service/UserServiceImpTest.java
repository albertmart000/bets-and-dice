package com.betsanddice.user.service;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.dto.GenericResultDto;
import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.exception.BadUuidException;
import com.betsanddice.user.exception.UserNotFoundException;
import com.betsanddice.user.helper.DocumentToDtoConverter;
import com.betsanddice.user.repository.UserRepository;
import com.betsanddice.user.utils.StringToUuidValidator;
import org.junit.jupiter.api.Assertions;
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
    private DocumentToDtoConverter<UserDocument, UserDto> converter;

    @Mock
    private StringToUuidValidator uuidValidator;

    @InjectMocks
    private UserServiceImp userService;

    private final String validId = "706507d4-b89f-41eb-a7eb-41838d08a08f";
    private final UUID userUuid = UUID.fromString(validId);
    private final String invalidUuid = "invalid-uuid";
    UserDocument userDocument = new UserDocument();
    UserDto userDto = new UserDto();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(uuidValidator.validateUuid(validId)).thenReturn(Mono.just(userUuid));
        when(uuidValidator.validateUuid(invalidUuid)).thenReturn(Mono.error(new BadUuidException("Invalid UUID")));
        when(userRepository.findByUuid(userUuid)).thenReturn(Mono.just(userDocument));
        when(converter.fromDocumentToDto(userDocument, UserDto.class)).thenReturn(userDto);
    }

    @Test
    void getUserById_ValidId_UserFound() {
        Mono<UserDto> resultDto = userService.getUserById(validId);

        StepVerifier.create(resultDto)
                .expectNext(userDto)
                .expectComplete()
                .verify();

        verify(uuidValidator).validateUuid(validId);
        verify(userRepository).findByUuid(userUuid);
        verify(converter).fromDocumentToDto(userDocument, UserDto.class);
    }

    @Test
    void getUserById_InvalidId_ErrorThrown() {

        Mono<UserDto> result = userService.getUserById(invalidUuid);

        StepVerifier.create(result)
                .expectError(BadUuidException.class)
                .verify();

        verifyNoInteractions(userRepository);
        verifyNoInteractions(converter);
    }

    @Test
    void getUserById_NonExistId_ErrorThrown() {
        String nonExistId = "4f8a6c91-8a9d-49b0-9f2c-3e67d2b18b7d";
        UUID nonExistUuid = UUID.fromString(nonExistId);

        when(uuidValidator.validateUuid(nonExistId)).thenReturn(Mono.just(nonExistUuid));
        when(userRepository.findByUuid(nonExistUuid)).thenReturn(Mono.empty());

        Mono<UserDto> result = userService.getUserById(nonExistId);

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof UserNotFoundException
                                && error.getMessage().equals("User with id " + nonExistId + " not found.")
                );
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

        int offset = 1;
        int limit = 2;

        when(userRepository.findAllByUuidNotNullExcludingTestingValues())
                .thenReturn(Flux.just(userDocument1, userDocument2, userDocument3, userDocument4));
        when(converter.fromDocumentFluxToDtoFlux(any(), any()))
                .thenReturn(Flux.just(userDto1, userDto2, userDto3, userDto4));
        when(userRepository.count()).thenReturn(Mono.just(100L));

        Mono<GenericResultDto<UserDto>> result = userService.getAllUsers(offset, limit);

        verify(userRepository).findAllByUuidNotNullExcludingTestingValues();
        verify(converter).fromDocumentFluxToDtoFlux(any(), any());

        StepVerifier.create(result)
                .expectSubscription()
                .assertNext(resultDto -> {
                    Assertions.assertEquals(100, resultDto.getCount());
                    Assertions.assertEquals(offset, resultDto.getOffset());
                    Assertions.assertEquals(limit, resultDto.getLimit());
                    Assertions.assertEquals(4, resultDto.getResults().length);
                    Assertions.assertEquals(userDto1, resultDto.getResults()[0]);
                    Assertions.assertEquals(userDto2, resultDto.getResults()[1]);
                    Assertions.assertEquals(userDto3, resultDto.getResults()[2]);
                    Assertions.assertEquals(userDto4, resultDto.getResults()[3]);
                })
                .expectComplete()
                .verify();
    }

}