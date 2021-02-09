package com.lyit.calculator.rest;

import com.lyit.calculator.service.ICalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CalculatorRestController {

    @Autowired
    private ICalculatorService calculatorService;

    @GetMapping(value = "/add/{numA}/{numB}")
    public int add(@PathVariable("numA") int numA,
                   @PathVariable("numB") int numB) {
        return calculatorService.add(numA, numB);
    }

    @GetMapping(value = "/subtract/{numA}/{numB}")
    public int subtract(@PathVariable("numA") int numA,
                   @PathVariable("numB") int numB) {
        return calculatorService.subtract(numA, numB);
    }

    @GetMapping(value = "/divide/{numA}/{numB}")
    public int divide(@PathVariable("numA") int numA,
                   @PathVariable("numB") int numB) {
        return calculatorService.divide(numA, numB);
    }

    @GetMapping(value = "/multiply/{numA}/{numB}")
    public int multiply(@PathVariable("numA") int numA,
                   @PathVariable("numB") int numB) {
        return calculatorService.multiply(numA, numB);
    }
}
