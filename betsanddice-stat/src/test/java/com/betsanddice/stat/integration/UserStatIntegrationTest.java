package com.betsanddice.stat.integration;

import com.betsanddice.stat.document.UserStatDocument;
import com.betsanddice.stat.repository.UserStatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UserStatIntegrationTest {

    private final String STAT_BASE_URL = "/betsanddice/api/v1/stat";

    @Container
    static MongoDBContainer container = new MongoDBContainer("mongo")
            .withExposedPorts(27017)
            .withStartupTimeout(Duration.ofSeconds(60));

    @DynamicPropertySource
    static void initMongoProperties(DynamicPropertyRegistry registry) {
        System.out.println("container url: {}" + container.getReplicaSetUrl("stat"));
        System.out.println("container host/port: {}/{}" + container.getHost() + " - " + container.getFirstMappedPort());
        registry.add("spring.data.mongodb.uri", () -> container.getReplicaSetUrl("stat"));
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserStatRepository userStatRepository;

    @BeforeEach
    void setup() {
        userStatRepository.deleteAll().block();

        UUID uuidUserStat1 = UUID.fromString("c73a00ef-bfb1-458a-9c9d-5b1cdfca4a01");
        UUID uuidUserStat2 = UUID.fromString("76628e83-b879-4186-8263-3325236a5fa5");

        UUID uuidUser = UUID.fromString("706507d4-b89f-41eb-a7eb-41838d08a08f");

        UUID uuidGame1 = UUID.fromString("c8a5440d-6466-463a-bccc-7fefbe9396e4");
        UUID uuidGame2 = UUID.fromString("9cc65b00-8412-46e7-ba6f-ead17a9fe167");

        double average = 3.5;

        UserStatDocument userStatDocument1 = new UserStatDocument(uuidUserStat1, uuidUser, uuidGame1, average);
        UserStatDocument userStatDocument2 = new UserStatDocument(uuidUserStat2, uuidUser, uuidGame2, average);

        userStatRepository.saveAll(Flux.just(userStatDocument1, userStatDocument2)).blockLast();
    }

    @Test
    void test() {
        webTestClient.get()
                .uri(STAT_BASE_URL + "/test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(String::toString, equalTo("Hello from Stat!!!"));
    }

    @Test
    void getAllUsers_UsersExist_UsersReturned() {
        webTestClient.get()
                .uri(STAT_BASE_URL + "/userStats")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserStatDocument.class)
                .hasSize(2);
    }

}