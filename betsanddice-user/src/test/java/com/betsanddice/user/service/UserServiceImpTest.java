package com.betsanddice.user.service;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.helper.UserDocumentToDtoConverter;
import com.betsanddice.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void getAllUsers_UsersExist_UsersReturned() {
        UserDto userDto1 = new UserDto();
        UserDto userDto2 = new UserDto();
        UserDto[] expectedUsers = {userDto1, userDto2};

        when(userRepository.findAll()).thenReturn(Flux.just(new UserDocument(), new UserDocument()));
        when(converter.convertDocumentFluxToDtoFlux(any())).thenReturn(Flux.just(userDto1, userDto2));

        Flux<UserDto> result = userService.getAllUsers();

        StepVerifier.create(result)
                .expectNext(expectedUsers)
                .expectComplete()
                .verify();

        verify(userRepository).findAll();
        verify(converter).convertDocumentFluxToDtoFlux(any());
    }

}