package com.lyit.calculator.service;

import org.springframework.stereotype.Service;

@Service
public class CalculatorServiceImpl implements ICalculatorService{
    /**
     * Method to perform addition
     *
     * @param numA
     * @param numB
     * @return
     */
    @Override
    public int add(int numA, int numB) {
        return numA + numA;
    }

    /**
     * Method to perform subtraction
     *
     * @param numA
     * @param numB
     * @return
     */
    @Override
    public int subtract(int numA, int numB) {
        return numA - numB;
    }

    /**
     * Method to perform division
     *
     * @param numA
     * @param numB
     * @return
     */
    @Override
    public int divide(int numA, int numB) {
        return numA / numB;
    }

    /**
     * Method to perform multiplication
     *
     * @param numA
     * @param numB
     * @return
     */
    @Override
    public int multiply(int numA, int numB) {
        return numA * numB;
    }
}
