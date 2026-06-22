package com.dellasse.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Classe principal da aplicação backend.
 */
@SpringBootApplication
public class BackendApplication {

	/**
	 * Ponto de entrada da aplicação.
	 *
	 * @param args Argumentos da linha de comando.
	 */
	public static void main(String[] args) {
		Dotenv env = Dotenv.configure().ignoreIfMissing().load();
		System.setProperty("DB_USERNAME", env.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", env.get("DB_PASSWORD"));
		SpringApplication.run(BackendApplication.class, args);
	}

}
