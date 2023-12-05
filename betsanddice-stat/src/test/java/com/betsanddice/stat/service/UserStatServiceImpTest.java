package com.betsanddice.stat.service;

import com.betsanddice.stat.document.UserStatDocument;
import com.betsanddice.stat.dto.UserStatDto;
import com.betsanddice.stat.helper.DocumentToDtoConverter;
import com.betsanddice.stat.repository.UserStatRepository;
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

class UserStatServiceImpTest {

    @Mock
    private UserStatRepository userStatRepository;

    @Mock
    private DocumentToDtoConverter<UserStatDocument, UserStatDto> converter;

    @InjectMocks
    private UserStatServiceImp userStatService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void getAllUserStats_UserStatsExist_UserStatsReturned_Test() {
        UserStatDto userStatDto1 = new UserStatDto();
        UserStatDto userStatDto2 = new UserStatDto();
        UserStatDto[] expectedGameTutorials = {userStatDto1, userStatDto2};

        when(userStatRepository.findAll()).thenReturn(Flux.just(new UserStatDocument(), new UserStatDocument()));
        when(converter.fromDocumentFluxToDtoFlux(any(), any())).thenReturn(Flux.just(userStatDto1, userStatDto2));

        Flux<UserStatDto> result = userStatService.getAllUserStats();

        StepVerifier.create(result)
                .expectNext(expectedGameTutorials)
                .expectComplete()
                .verify();

        verify(userStatRepository).findAll();
        verify(converter).fromDocumentFluxToDtoFlux(any(), any());
    }

}