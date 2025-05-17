package com.betsanddice.user.controller;

import com.betsanddice.user.dto.GenericResultDto;
import com.betsanddice.user.dto.UserCrapsGameStatsDto;
import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.dto.UserRegisterDto;
import com.betsanddice.user.exception.UserAlreadyExistException;
import com.betsanddice.user.service.IUserService;
import com.betsanddice.user.service.client.ICrapsClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CONFLICT;

@WebFluxTest(UserController.class)
class UserControllerTest {

    private final String USER_BASE_URL = "/api";

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private IUserService userService;

    @MockitoBean
    private ICrapsClientService crapsGameClientService;

    UserDto userDto1 = new UserDto();
    UserDto userDto2 = new UserDto();
    UserDto userDto3 = new UserDto();

    @Test
    void test() {
        webTestClient.get()
                .uri(USER_BASE_URL + "/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hello from User!!!");
    }

    @Test
    void registerUser_ValidUser_UserRegistered() {
        UUID userUuid = UUID.randomUUID();

        UserRegisterDto userRegisterDto = new UserRegisterDto("Morrow", "Montgomery", "Player1", "morrowmontgomery@email.com",
                "player1", LocalDate.parse("2000-03-03"));

        UserDto expectedUserDto = new UserDto(userUuid, "Morrow", "Montgomery", "Player1", "morrowmontgomery@email.com",
                "player1", "2000-03-03", "2025-05-11T00:00:00");

        when(userService.registerUser(any())).thenReturn(Mono.just(expectedUserDto));

        webTestClient.post()
                .uri(USER_BASE_URL + "/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRegisterDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(userDto -> {
                    assert userDto.getUuid().equals(expectedUserDto.getUuid());
                    assert userDto.getEmail().equals(expectedUserDto.getEmail());
                    assert userDto.getNickname().equals(expectedUserDto.getNickname());
                });

        verify(userService).registerUser(any(UserRegisterDto.class));
    }

    @Test
    void registerUser_InvalidUser_BadRequest() {

        UserRegisterDto invalidUserRegisterDto = new UserRegisterDto("", "Montgomery", "Player1", "morrowmontgomery@email.com",
                "player1", LocalDate.parse("2000-03-03"));

        webTestClient.post()
                .uri(USER_BASE_URL + "/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidUserRegisterDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void registerUser_ExistingUser_Conflict() {

        UserRegisterDto userRegisterDto = new UserRegisterDto("Morrow", "Montgomery", "Player1", "player1@email.com",
                "player1", LocalDate.parse("2000-03-03"));

        when(userService.registerUser(any())).thenReturn(Mono.error(new UserAlreadyExistException("User with email player1@email.com already exists")));

        webTestClient.post()
                .uri(USER_BASE_URL + "/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRegisterDto)
                .exchange()
                .expectStatus().isEqualTo(CONFLICT)
                .expectBody(String.class)
                .value(errorMessage -> {
                    assert errorMessage.contains("User with email player1@email.com already exists");
                });

        verify(userService).registerUser(any(UserRegisterDto.class));
    }

    @Test
    void getUser_ValidId_UserReturned() {
        String userId = "valid-user-id";
        UserDto expectedUserDto = new UserDto();

        when(userService.getUserById(userId)).thenReturn(Mono.just(expectedUserDto));

        webTestClient.get()
                .uri(USER_BASE_URL + "/users/userById/{userId}", userId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(dto -> {
                    assert dto != null;
                });
    }

    @Test
    void getUser_ValidEmail_UserReturned() {
        String email= "admin@email.com";
        UserDto expectedUserDto = new UserDto();

        when(userService.getUserByEmail(email)).thenReturn(Mono.just(expectedUserDto));

        webTestClient.get()
                .uri(USER_BASE_URL + "/users/userByEmail/{email}", email)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(dto -> {
                    assert dto != null;
                });
    }

    @Test
    void getAllUsers_ValidPageParameters_UsersReturned() {
        UserDto[] expectedUsers = {userDto1, userDto2, userDto3};
        GenericResultDto<UserDto> expectedResult = new GenericResultDto<>();
        expectedResult.setInfo(0, 3, 3, expectedUsers);

        Mono<GenericResultDto<UserDto>> expectedResultMono = Mono.just(expectedResult);

        String offset = "0";
        String limit = "3";

        when(userService.getAllUsers(Integer.parseInt(offset), Integer.parseInt(limit)))
                .thenReturn(expectedResultMono);

        webTestClient.get()
                .uri(USER_BASE_URL + "/users?offset=0&limit=3")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDto.class);
    }

    @Test
    void getAllUsers_NullPageParameters_UsersReturned() {
        UserDto[] expectedUsers = {userDto1, userDto2};
        GenericResultDto<UserDto> expectedResult = new GenericResultDto<>();
        expectedResult.setInfo(0, 2, 2, expectedUsers);

        Mono<GenericResultDto<UserDto>> expectedResultMono = Mono.just(expectedResult);

        String offset = "0";
        String limit = "2";

        when(userService.getAllUsers(Integer.parseInt(offset), Integer.parseInt(limit)))
                .thenReturn(expectedResultMono);

        webTestClient.get()
                .uri(USER_BASE_URL + "/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDto.class);
    }

    @Test
    void getUserCrapsGameStats_CrapsGameStatsReturned() {
        String userId = "706507d4-b89f-41eb-a7eb-41838d08a08f";

        UserCrapsGameStatsDto expectedUserCrapsGameStatsDto = new UserCrapsGameStatsDto();

        when(crapsGameClientService.getUserCrapsGameStats(userId))
                .thenReturn(Mono.just(expectedUserCrapsGameStatsDto));

        webTestClient.get()
                .uri(USER_BASE_URL + "/crapsGamesStatsByUser/{userid}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserCrapsGameStatsDto.class);
    }
}