package com.betsanddice.user.controller;

import com.betsanddice.user.dto.GenericResultDto;
import com.betsanddice.user.dto.UserCrapsGameStatsDto;
import com.betsanddice.user.dto.UserDto;
import com.betsanddice.user.service.IUserService;
import com.betsanddice.user.service.client.ICrapsGameClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(UserController.class)
class UserControllerTest {

    private final String USER_BASE_URL = "/betsanddice/api/v1/user";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private IUserService userService;

    @MockBean
    private ICrapsGameClientService crapsGameClientService;

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
    void getOneUser_ValidId_UserReturned() {
        String userId = "valid-user-id";
        UserDto expectedUserDto = new UserDto();

        when(userService.getUserById(userId)).thenReturn(Mono.just(expectedUserDto));

        webTestClient.get()
                .uri(USER_BASE_URL + "/users/{userId}", userId)
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
                .uri("/betsanddice/api/v1/user/users?offset=0&limit=3")
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
                .uri("/betsanddice/api/v1/user/users")
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