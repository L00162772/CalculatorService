package com.lyit.calculator.rest;

import com.lyit.calculator.utils.Constants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthRestController {

    @GetMapping(value = "")
    public String health() {
        return Constants.SERVER_IS_UP;
    }

}
