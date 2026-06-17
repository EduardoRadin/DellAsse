package com.dellasse.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootTest
class BackendApplicationTests {

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		Dotenv dotenv = Dotenv.load();
		registry.add("spring.datasource.username", () -> dotenv.get("DB_USERNAME"));
		registry.add("spring.datasource.password", () -> dotenv.get("DB_PASSWORD"));
	}

	@Test
	void contextLoads() {
	}

}
