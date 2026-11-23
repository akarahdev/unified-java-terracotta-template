package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnifiedController {
    @GetMapping("/v1/hello")
    public String hello() {
        return "Hello there!";
    }
}
