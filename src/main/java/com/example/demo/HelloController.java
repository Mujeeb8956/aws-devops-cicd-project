
package com.example.demo;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
            "application", "RS Enterprise",
            "message", "Hello RS Enterprise!",
            "greeting", "Hello Ragini Ma'am",
            "environment", "AWS DevOps CI/CD Environment"
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
            "message", "Hello RS Enterprise and Ragini Maal!"
        );
    }

    @GetMapping("/api/info")
    public Map<String, String> info() {
        return Map.of(
            "project", "RS Enterprise AWS DevOps CI/CD",
            "version", "1.0.0",
            "environment", "development"
        );
    }
}

