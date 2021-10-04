package com.lyit.calculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class CalculatorServiceTest {

    ICalculatorService calculatorService;

    @BeforeEach
    public void init() {
        calculatorService = new CalculatorServiceImpl();
    }

    @Test
    public void testAddition() {
        int numA = 5;
        int numB = 6;
        int expectedResult = 12;
        int result = calculatorService.add(numA, numB);
        assertEquals(result, expectedResult);
    }

    @Test
    public void testSubtraction() {
        int numA = 35;
        int numB = 6;
        int expectedResult = 29;
        int result = calculatorService.subtract(numA, numB);
        assertEquals(result, expectedResult);
    }

    @Test
    public void testDivision() {
        int numA = 30;
        int numB = 5;
        int expectedResult = 6;
        int result = calculatorService.divide(numA, numB);
        assertEquals(result, expectedResult);
    }

    @Test
    public void testMultiplication() {
        int numA = 5;
        int numB = 6;
        int expectedResult = 30;
        int result = calculatorService.multiply(numA, numB);
        assertEquals(result, expectedResult);
    }
}
