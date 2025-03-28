package com.betsanddice.craps.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DeleteResponseDtoTest {
    @Test
    void testMessageAndID() {
        String id = "valid_id";
        String message = "Expected message";

        DeleteResponseDto deleteResponseDto = new DeleteResponseDto(id,message);

        Assertions.assertEquals(message, deleteResponseDto.getMessage());
        Assertions.assertEquals(id, deleteResponseDto.getId());
    }

}