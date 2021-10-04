package com.lyit.calculator.rest;

import com.lyit.calculator.service.ICalculatorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculatorRestController.class)
public class CalculatorRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ICalculatorService calculatorService;

    @Test
    public void testAddition() throws Exception {
        int numA = 5;
        int numB = 6;
        int result = 11;

        Mockito.when(calculatorService.add(Mockito.anyInt(), Mockito.anyInt())).thenReturn(result);

        mvc.perform(get("/add/{numA}/{numB}", numA, numB)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").value(result)).andReturn();

        Mockito.verify(calculatorService, Mockito.times(1)).add(Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void testSubtraction() throws Exception {
        int numA = 11;
        int numB = 6;
        int result = 5;

        Mockito.when(calculatorService.subtract(Mockito.anyInt(), Mockito.anyInt())).thenReturn(result);

        mvc.perform(get("/subtract/{numA}/{numB}", numA, numB)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").value(result)).andReturn();

        Mockito.verify(calculatorService, Mockito.times(1)).subtract(Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void testDivision() throws Exception {
        int numA = 12;
        int numB = 6;
        int result = 2;

        Mockito.when(calculatorService.divide(Mockito.anyInt(), Mockito.anyInt())).thenReturn(result);

        mvc.perform(get("/divide/{numA}/{numB}", numA, numB)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").value(result)).andReturn();

        Mockito.verify(calculatorService, Mockito.times(1)).divide(Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void testMultiplication() throws Exception {
        int numA = 6;
        int numB = 6;
        int result = 32;

        Mockito.when(calculatorService.multiply(Mockito.anyInt(), Mockito.anyInt())).thenReturn(result);

        mvc.perform(get("/multiply/{numA}/{numB}", numA, numB)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").value(result)).andReturn();

        Mockito.verify(calculatorService, Mockito.times(1)).multiply(Mockito.anyInt(), Mockito.anyInt());
    }
}
