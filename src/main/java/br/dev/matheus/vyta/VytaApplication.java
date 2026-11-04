package br.dev.matheus.vyta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VytaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VytaApplication.class, args);
	}

}
