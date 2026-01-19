package com.trangnx.saver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MoneySaverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneySaverApplication.class, args);
	}

}
