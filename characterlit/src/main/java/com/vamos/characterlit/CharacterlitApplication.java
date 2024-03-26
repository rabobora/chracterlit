package com.vamos.characterlit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CharacterlitApplication {

	public static void main(String[] args) {
		SpringApplication.run(CharacterlitApplication.class, args);
	}

}
