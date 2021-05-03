package edu.uta.cse5381.assignment3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Assignment01Application {

	public static void main(String[] args) {
		SpringApplication.run(Assignment01Application.class, args);
	}

}
