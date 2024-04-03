package com.betsanddice.stat.service;

import com.betsanddice.stat.document.UserGameStatDocument;
import com.betsanddice.stat.dto.UserGameStatDto;
import com.betsanddice.stat.helper.DocumentToDtoConverter;
import com.betsanddice.stat.repository.UserGameStatRepository;
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

class UserGameStatServiceImpTest {

    @Mock
    private UserGameStatRepository userGameStatRepository;

    @Mock
    private DocumentToDtoConverter<UserGameStatDocument, UserGameStatDto> converter;

    @InjectMocks
    private UserGameStatServiceImp userStatService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void getAllUserStats_UserStatsExist_UserStatsReturned_Test() {
        UserGameStatDto userGameStatDto1 = new UserGameStatDto();
        UserGameStatDto userGameStatDto2 = new UserGameStatDto();
        UserGameStatDto[] expectedGameTutorials = {userGameStatDto1, userGameStatDto2};

        when(userGameStatRepository.findAll()).thenReturn(Flux.just(new UserGameStatDocument(), new UserGameStatDocument()));
        when(converter.fromDocumentFluxToDtoFlux(any(), any())).thenReturn(Flux.just(userGameStatDto1, userGameStatDto2));

        Flux<UserGameStatDto> result = userStatService.getAllUserStats();

        StepVerifier.create(result)
                .expectNext(expectedGameTutorials)
                .expectComplete()
                .verify();

        verify(userGameStatRepository).findAll();
        verify(converter).fromDocumentFluxToDtoFlux(any(), any());
    }

}