package com.fdmgroup.apmproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main application class for the ApmProject.
 *
 * This class is responsible for bootstrapping the Spring Boot application.
 * It includes the following annotations:
 *
 * - `@SpringBootApplication`: This annotation indicates that this class is a Spring Boot application.
 *   It enables auto-configuration, component scanning, and other features of Spring Boot.
 *
 *
 * The `main()` method is the entry point for the application. It starts the Spring Boot application
 * by calling `SpringApplication.run()`.
 *
 * @author 
 * @version 1.0
 * @since 2024-04-22
 */
@SpringBootApplication

public class ApmProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApmProjectApplication.class, args);
    }
}
