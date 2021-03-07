package com.lyit.calculator.rest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CalculatorRestControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAddition() throws Exception {
        int numA = 5;
        int numB = 6;
        int result = numA + numB;
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/add/" + numA + "/" + numB, Integer.class)).isEqualTo(result);
    }

    @Test
    public void testSubstraction() throws Exception {
        int numA = 50;
        int numB = 6;
        int result = numA - numB;
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/subtract/" + numA + "/" + numB, Integer.class)).isEqualTo(result);
    }

    @Test
    public void testDivision() throws Exception {
        int numA = 55;
        int numB = 5;
        int result = numA / numB;
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/divide/" + numA + "/" + numB, Integer.class)).isEqualTo(result);
    }

    @Test
    public void testMultiplication() throws Exception {
        System.out.println("Here I AM");
        int numA = 55;
        int numB = 5;
        int result = numA * numB;
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/multiply/" + numA + "/" + numB, Integer.class)).isEqualTo(result);
    }
}
