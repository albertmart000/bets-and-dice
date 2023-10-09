package com.betsanddice.user.service;

import com.betsanddice.user.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements  IUserService{

    @Autowired
    private IUserRepository userRepository;
}
