package com.betsanddice.user.service;

import com.betsanddice.user.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements  IUserService{

    private static final Logger log = LoggerFactory.getLogger(UserServiceImp.class);

    @Autowired
    private IUserRepository userRepository;
}
