package com.betsanddice.stat.service;

import com.betsanddice.stat.document.UserStatDocument;
import com.betsanddice.stat.dto.UserStatDto;
import com.betsanddice.stat.helper.DocumentToDtoConverter;
import com.betsanddice.stat.repository.UserStatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class UserStatServiceImp implements IUserStatService {

    private static final Logger log = LoggerFactory.getLogger(UserStatServiceImp.class);

    @Autowired
    private UserStatRepository userStatRepository;

    @Autowired
    private DocumentToDtoConverter<UserStatDocument, UserStatDto> userStatConverter = new DocumentToDtoConverter<>();

    @Override
    public Flux<UserStatDto> getAllUserStats() {
        Flux<UserStatDocument> userStatList = userStatRepository.findAll();
        return userStatConverter.fromDocumentFluxToDtoFlux(userStatList, UserStatDto.class);


    }
}
