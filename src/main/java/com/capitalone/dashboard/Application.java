package com.capitalone.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Application configuration and bootstrap
 */
@SpringBootApplication
@EnableRetry
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}
}
