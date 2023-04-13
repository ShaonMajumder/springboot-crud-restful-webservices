package net.javaguides.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = {"BACKEND.Models"})
@SpringBootApplication
public class SpringbootCrudRestfulWebservicesApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringbootCrudRestfulWebservicesApplication.class, args);
	}
}
