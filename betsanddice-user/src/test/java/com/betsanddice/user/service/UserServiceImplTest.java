package com.betsanddice.user.service;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.dto.GenericResultDto;
import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.dto.UserRegisterDto;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private DocumentToDtoConverter<UserDocument, UserDto> converter;

    @Mock
    private StringToUuidValidator uuidValidator;

    @InjectMocks
    private UserServiceImpl userService;

    UserDocument userDocument = new UserDocument();
    UserDto userDto = new UserDto();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void registerUser_NewAndValidParams_UserRegisteredSuccessfully() {
        UserRegisterDto userRegisterDto = new UserRegisterDto("Morrow", "Montgomery", "Player1",
                "player1@email.com", "player1", LocalDate.parse("2000-03-03"));

        userDocument = UserDocument.builder()
                .uuid(UUID.randomUUID())
                .firstName("Morrow")
                .surname("Montgomery")
                .nickname("Player1")
                .email("player1@email.com")
                .password("player1")
                .build();

        UserDto expectedUserDto = new UserDto(userDocument.getUuid(), "Morrow", "Montgomery", "Player1",
                "player1@email.com", "player1", "2000-03-03", LocalDateTime.now().toString()
        );

        when(userRepository.findByEmail(userRegisterDto.getEmail())).thenReturn(Mono.empty());
        when(userRepository.findByNickname(userRegisterDto.getNickname())).thenReturn(Mono.empty());
        when(userRepository.save(any(UserDocument.class))).thenReturn(Mono.just(userDocument));
        when(converter.fromDocumentToDto(userDocument, UserDto.class)).thenReturn(expectedUserDto);

        Mono<UserDto> result = userService.registerUser(userRegisterDto);

        StepVerifier.create(result)
                .expectNext(expectedUserDto)
                .expectComplete()
                .verify();

        verify(userRepository).findByEmail(userRegisterDto.getEmail());
        verify(userRepository).findByNickname(userRegisterDto.getNickname());
        verify(userRepository).save(any(UserDocument.class));
        verify(converter).fromDocumentToDto(userDocument, UserDto.class);
    }

    @Test
    void getUserById_ValidId_UserFound() {
        String validId = "706507d4-b89f-41eb-a7eb-41838d08a08f";
        UUID userUuid = UUID.fromString(validId);

        when(uuidValidator.validateUuid(validId)).thenReturn(Mono.just(userUuid));
        when(userRepository.findByUuid(userUuid)).thenReturn(Mono.just(userDocument));
        when(converter.fromDocumentToDto(userDocument, UserDto.class)).thenReturn(userDto);

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
        String invalidUuid = "invalid-uuid";

        when(uuidValidator.validateUuid(invalidUuid)).thenReturn(Mono.error(new BadUuidException("Invalid UUID")));
        when(userRepository.findByUuid(any(UUID.class))).thenReturn(Mono.empty());

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
    void getUserByEmail_ValidEmail_UserFound() {
        String validEmail = "valid@email.com";

        when(userRepository.findByEmail(validEmail)).thenReturn(Mono.just(userDocument));
        when(converter.fromDocumentToDto(userDocument, UserDto.class)).thenReturn(userDto);

        Mono<UserDto> resultDto = userService.getUserByEmail(validEmail);
        StepVerifier.create(resultDto)
                .expectNext(userDto)
                .expectComplete()
                .verify();

        verify(userRepository).findByEmail(validEmail);
        verify(converter).fromDocumentToDto(userDocument, UserDto.class);
    }

    @Test
    void getUserByEmail_NonExistId_ErrorThrown() {
        String nonExistEmail = "nonExist@email";

        when(userRepository.findByEmail(nonExistEmail)).thenReturn(Mono.empty());
        when(converter.fromDocumentToDto(userDocument, UserDto.class)).thenReturn(userDto);
        when(userRepository.findByEmail(nonExistEmail)).thenReturn(Mono.empty());

        Mono<UserDto> result = userService.getUserByEmail(nonExistEmail);

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof UserNotFoundException
                                && error.getMessage().equals("User with email " + nonExistEmail + " not found.")
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