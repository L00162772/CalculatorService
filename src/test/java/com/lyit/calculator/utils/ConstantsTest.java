package com.lyit.calculator.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConstantsTest {

    @Test
    public void testConstructor() {
        Constants constants = new Constants();
        assertNotNull(constants);
    }
}
