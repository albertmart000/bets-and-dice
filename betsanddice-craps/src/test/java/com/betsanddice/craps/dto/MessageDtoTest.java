package com.betsanddice.craps.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MessageDtoTest {
    @Test
    void testMessage() {
        String message = "Expected message";

        MessageDto errorResponseMessage = new MessageDto(message);

        assertEquals(message, errorResponseMessage.getMessage());
    }

    @Test
    void testNotExpectedMessage() {
        String message = "Expected message";
        String notExpectedMessage = "Not expected message.";

        MessageDto errorResponseMessage = new MessageDto(message);

        assertNotEquals(notExpectedMessage, errorResponseMessage.getMessage());
    }
}