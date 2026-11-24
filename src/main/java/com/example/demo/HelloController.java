
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
        return Map.of(
            "status", "UP",
            "message", "Application is healthy"
        );
    }
      @GetMapping("/api/hello")
      public Map<String, String> hello() {

        return Map.of(
            "message",
            "Hello from the AWS CI/CD project!"
        );
    }

    @GetMapping("/api/info")
    public Map<String, String> info() {
        return Map.of(
            "project", "AWS DevOps CI/CD",
            "version", "1.0.0",
            "environment", "development"
        );
    }
}

