package com.betsanddice.user.controller;

import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping(value = "/betsanddice/api/v1/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final String NO_SERVICE = "No Services";

    IUserService userService;

    private DiscoveryClient discoveryClient;

    public UserController(IUserService userService, DiscoveryClient discoveryClient) {
        this.userService = userService;
        this.discoveryClient = discoveryClient;
    }

    @Operation(summary = "Testing the App")
    @GetMapping(value = "/test")
    public String test() {
        log.info("** Greetings from the logger **");

        Optional<String> userService = discoveryClient.getInstances("betsanddice-user")
                .stream()
                .findAny()
                .map(Object::toString);

        Optional<String> crapsService = discoveryClient.getInstances("betsanddice-craps")
                .stream()
                .findAny()
                .map(Object::toString);

        Optional<String> tutorialService = discoveryClient.getInstances("betsanddice-tutorial")
                .stream()
                .findAny()
                .map(Object::toString);

        log.info("~~~~~~~~~~~~~~~~~~~~~~");
        log.info("Scanning micros:");
        log.info((userService.orElse(NO_SERVICE))
                .concat(System.lineSeparator())
                .concat(crapsService.orElse(NO_SERVICE))
                .concat(System.lineSeparator())
                .concat(tutorialService.orElse(NO_SERVICE)));
        log.info("~~~~~~~~~~~~~~~~~~~~~~");

        return "Hello from Bets And Dice!!!";
    }

    @GetMapping(path = "/users/{userId}")
    @Operation(
            operationId = "Get the information from a chosen user.",
            summary = "Get to see the User Data.",
            description = "Sending the ID User through the URI to retrieve it from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "404", description = "The User with given Id was not found.", content = {@Content(schema = @Schema())})
            }
    )
    public Mono<UserDto> getOneUser(@PathVariable("userId") String id) {
        return userService.getUserByUuid(id);
    }

    @GetMapping("/users")
    @Operation(
            operationId = "Get all the stored users into the Database.",
            summary = "Get to see users.",
            description = "Requesting all the users through the URI from the database.",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")}),
                    @ApiResponse(responseCode = "404", description = "No users were found.", content = {@Content(schema = @Schema())})
            })
    public Flux<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

}
