package com.ballersApi.ballersApi;

import com.ballersApi.ballersApi.models.Player;
import com.ballersApi.ballersApi.models.User;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import com.ballersApi.ballersApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BallersApiApplication {

	@Value("${server.port}")
	private String serverPort;

	public static void main(String[] args) {
		SpringApplication.run(BallersApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			UserRepository userRepository
	) {
		return args -> {


			System.out.println("Server running on port " + serverPort);
		};
	}

}
