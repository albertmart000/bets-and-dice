package com.betsanddice.user.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MessageDtoTest {
    @Test
    void testConstructorAndGetter() {
        String testMessage = "Message";
        MessageDto messageDto = new MessageDto(testMessage);

        Assertions.assertEquals(testMessage, messageDto.getMessage());
    }

    @Test
    void testSetterAndGetter() {
        MessageDto messageDto = new MessageDto("Initial Message");
        String newMessage = "Updated Message";
        messageDto.setMessage(newMessage);

        Assertions.assertEquals(newMessage, messageDto.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        String testMessage = "Same message";
        MessageDto messageDto1 = new MessageDto(testMessage);
        MessageDto messageDto2 = new MessageDto(testMessage);
        MessageDto messageDto3 = new MessageDto("Different Message");

        Assertions.assertEquals(messageDto1, messageDto2);
        Assertions.assertEquals(messageDto1.hashCode(), messageDto2.hashCode());
        assertNotEquals(messageDto1, messageDto3);
    }

    @Test
    void testMessage() {
        String message = "Expected message";

        MessageDto errorResponseMessage = new MessageDto(message);

        Assertions.assertEquals(message, errorResponseMessage.getMessage());
    }

    @Test
    void testNotExpectedMessage() {
        String message = "Expected message";
        String notExpectedMessage = "Not expected message.";

        MessageDto errorResponseMessage = new MessageDto(message);

        assertNotEquals(notExpectedMessage, errorResponseMessage.getMessage());
    }

}