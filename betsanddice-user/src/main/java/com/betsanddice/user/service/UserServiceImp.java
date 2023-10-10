package com.betsanddice.user.service;

import com.betsanddice.user.document.UserDocument;
import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.helper.UserDocumentToDtoConverter;
import com.betsanddice.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class UserServiceImp implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImp.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDocumentToDtoConverter converter;

    public Flux<UserDto> getAllUsers() {
        Flux<UserDocument> usersList = userRepository.findAll();
        return converter.convertDocumentFluxToDtoFlux(usersList);
    }
}
