package com.ballersApi.ballersApi;

import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.repositries.UserRepositry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BallersApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BallersApiApplication.class, args);
	}

	@Bean
	public static CommandLineRunner commandLineRunner(
			UserRepositry userRepositry
	) {
		return args -> {
			User user = new User();

			user.setUsername("baller");
			user.setPassword("pp");

			userRepositry.save(user);

			System.out.println("Skibid");

			userRepositry.findAll().forEach(System.out::println);

		};


	}

}
