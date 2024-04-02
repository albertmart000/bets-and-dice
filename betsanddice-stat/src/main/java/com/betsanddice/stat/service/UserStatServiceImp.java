package com.betsanddice.stat.service;

import com.betsanddice.stat.document.UserGameStatDocument;
import com.betsanddice.stat.dto.UserGameStatDto;
import com.betsanddice.stat.helper.DocumentToDtoConverter;
import com.betsanddice.stat.repository.UserStatRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class UserStatServiceImp implements IUserStatService {

    private final UserStatRepository userStatRepository;
    private final DocumentToDtoConverter<UserGameStatDocument, UserGameStatDto> userStatConverter;

    public UserStatServiceImp(UserStatRepository userStatRepository, DocumentToDtoConverter<UserGameStatDocument, UserGameStatDto> userStatConverter) {
        this.userStatRepository = userStatRepository;
        this.userStatConverter = userStatConverter;
    }

    @Override
    public Flux<UserGameStatDto> getAllUserStats() {
        Flux<UserGameStatDocument> userStatList = userStatRepository.findAll();
        return userStatConverter.fromDocumentFluxToDtoFlux(userStatList, UserGameStatDto.class);
    }

}
