package com.betsanddice.stat.service;

import com.betsanddice.stat.document.UserGameStatDocument;
import com.betsanddice.stat.dto.UserGameStatDto;
import com.betsanddice.stat.helper.DocumentToDtoConverter;
import com.betsanddice.stat.repository.UserGameStatRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class UserGameStatServiceImp implements IUserGameStatService {

    private final UserGameStatRepository userGameStatRepository;
    private final DocumentToDtoConverter<UserGameStatDocument, UserGameStatDto> userGameStatConverter;

    public UserGameStatServiceImp(UserGameStatRepository userGameStatRepository, DocumentToDtoConverter<UserGameStatDocument, UserGameStatDto> userGameStatConverter) {
        this.userGameStatRepository = userGameStatRepository;
        this.userGameStatConverter = userGameStatConverter;
    }

    @Override
    public Flux<UserGameStatDto> getAllUserStats() {
        Flux<UserGameStatDocument> userGameStatList = userGameStatRepository.findAll();
        return userGameStatConverter.fromDocumentFluxToDtoFlux(userGameStatList, UserGameStatDto.class);
    }

}
