package com.betsanddice.craps.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ResourceHelperTest {

    @Test
    @DisplayName("Read a resource as String test")
    void readResourceAsStringTest (){
        ResourceHelper resourceHelper = new ResourceHelper("json/random.json");
        String expected = "{\"name\": \"RandomName\", \"num\": [1,2,3]}";
        assertEquals(expected, resourceHelper.readResourceAsString().get());
    }

    @Test
    void failedReadResourceTest () {
        String invalidPath = "somename/some.json";
        ResourceHelper resourceHelper = new ResourceHelper(invalidPath);
        assertNull(resourceHelper.readResourceAsString());
    }

}