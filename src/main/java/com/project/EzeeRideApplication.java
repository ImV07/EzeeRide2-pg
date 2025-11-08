package com.project;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@SpringBootApplication
@EnableScheduling
public class EzeeRideApplication {

    static {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("SMTP_USERNAME", dotenv.get("SMTP_USERNAME"));
        System.setProperty("SMTP_PASSWORD", dotenv.get("SMTP_PASSWORD"));
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
    }

	public static void main(String[] args) {
		SpringApplication.run(EzeeRideApplication.class, args);
	}

}
