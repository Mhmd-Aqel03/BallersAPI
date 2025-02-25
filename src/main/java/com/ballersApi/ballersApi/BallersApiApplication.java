package com.ballersApi.ballersApi;

import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BallersApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BallersApiApplication.class, args);
	}

	@Bean
	public static CommandLineRunner commandLineRunner(
			UserRepository userRepository
	) {
		return args -> {
			User user = new User();

			//user.setUsername("baller");
			//user.setPassword("pp");

			userRepository.save(user);

			System.out.println("Skibid");

			userRepository.findAll().forEach(System.out::println);

		};


	}

}
