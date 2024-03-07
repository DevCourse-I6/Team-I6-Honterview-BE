package com.i6.honterview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HonterviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(HonterviewApplication.class, args);
	}

}
