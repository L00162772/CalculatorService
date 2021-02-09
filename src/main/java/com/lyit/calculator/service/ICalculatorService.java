package com.lyit.calculator.service;

public interface ICalculatorService {

    /**
     * Method to perform addition
     * @param numA
     * @param numB
     * @return
     */
    int add(int numA, int numB);

    /**
     * Method to perform subtraction
     * @param numA
     * @param numB
     * @return
     */
    int subtract(int numA, int numB);

    /**
     * Method to perform division
     * @param numA
     * @param numB
     * @return
     */
    int divide(int numA, int numB);
    /**
     * Method to perform multiplication
     * @param numA
     * @param numB
     * @return
     */
    int multiply(int numA, int numB);

}
