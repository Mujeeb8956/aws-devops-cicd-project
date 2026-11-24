package com.example.demo;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public Map<String, String> home() {

        return Map.of(
            "application", "AWS DevOps CI/CD Demo",
            "message", "Hello! Your Java application is running.",
            "next_step", "We will automate build and deployment using Jenkins."
        );
    }

    @GetMapping("/api/health")
    public Map<String, String> health() {

        return Map.of("status", "UP");
    }

    @GetMapping("/api/hello")
    public Map<String, String> hello() {

        return Map.of(
            "message",
            "Hello from the AWS CI/CD project!"
        );
    }
}
