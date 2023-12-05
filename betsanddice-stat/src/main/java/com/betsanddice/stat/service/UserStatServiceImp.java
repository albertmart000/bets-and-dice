package com.betsanddice.stat.service;

import com.betsanddice.stat.document.UserStatDocument;
import com.betsanddice.stat.dto.UserStatDto;
import com.betsanddice.stat.helper.DocumentToDtoConverter;
import com.betsanddice.stat.repository.UserStatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class UserStatServiceImp implements IUserStatService {

    private static final Logger log = LoggerFactory.getLogger(UserStatServiceImp.class);

    private final UserStatRepository userStatRepository;
    private final DocumentToDtoConverter<UserStatDocument, UserStatDto> userStatConverter;

    public UserStatServiceImp(UserStatRepository userStatRepository, DocumentToDtoConverter<UserStatDocument, UserStatDto> userStatConverter) {
        this.userStatRepository = userStatRepository;
        this.userStatConverter = userStatConverter;
    }

    @Override
    public Flux<UserStatDto> getAllUserStats() {
        Flux<UserStatDocument> userStatList = userStatRepository.findAll();
        return userStatConverter.fromDocumentFluxToDtoFlux(userStatList, UserStatDto.class);
    }

}
