package com.assessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Main Application Class
 * 
 * - Entry point for the Spring Boot application.
 * - Configures and starts the RunLoyal Assessment application.
 * - Enables OpenAPI documentation using Swagger.
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "RunLoyal Assessment APIs",
        version = "1.0",
        description = "API documentation for RunLoyal Assessment"
))
public class RunLoyalAssessmentApplication {

    /**
     * Main method to launch the Spring Boot application.
     * 
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(RunLoyalAssessmentApplication.class, args);
    }
}
