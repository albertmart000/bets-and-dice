package com.betsanddice.auth.controller;

import com.betsanddice.auth.dto.User;
import com.betsanddice.auth.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping(value = "/api")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    private final IUserService userService;

    public AuthController(IUserService userService) {
        this.userService = userService;
    }

    // @Operation(summary = "Testing the App")
    @GetMapping(value = "/test")
    public String test() {
        log.info("** Greetings from the logger **");
        return "Hello from Auth!!!";
    }

    @GetMapping("/user/{email}")
    public Mono<ResponseEntity<User>> getUserDetails(@PathVariable("email") String email) {
        return userService.fetchUserData(email)
                .map(ResponseEntity.ok()::body);
    }

}