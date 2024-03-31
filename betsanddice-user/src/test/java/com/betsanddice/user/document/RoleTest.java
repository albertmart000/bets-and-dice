package com.betsanddice.user.document;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleTest {
    @Test
    void enumTest() {
        assertEquals("ADMIN", Role.ADMIN.name());
        assertEquals("PLAYER", Role.PLAYER.name());
    }
}