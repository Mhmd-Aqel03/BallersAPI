package com.ballersApi.ballersApi;

import com.ballersApi.ballersApi.models.*;
import com.ballersApi.ballersApi.repositories.CourtRepository;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import com.ballersApi.ballersApi.repositories.SessionRepository;
import com.ballersApi.ballersApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableAsync
public class BallersApiApplication {

	@Value("${server.port}")
	private String serverPort;

	public static void main(String[] args) {
		SpringApplication.run(BallersApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			UserRepository userRepository,
			SessionRepository sessionRepository,
			CourtRepository courtRepository
	) {
		return args -> {
			User user = new User();
			user.setUsername("username");
			user.setPassword("password98!");
			user.setRole(Role.ROLE_PLAYER);
			user.setEmail("admin@gmail.com");
			Player player = new Player();
			player.setPhoneNumber("23432");
			player.setPostion("CT");

			user.setPlayer(player);

			userRepository.save(user);

			User user1 = new User();
			user1.setUsername("ahmedKhalid");
			user1.setPassword("mohammagoat123!");
			user1.setRole(Role.ROLE_PLAYER);
			user1.setEmail("admin4@gmail.com");
			Player player1 = new Player();
			player1.setPhoneNumber("23432");
			player1.setPostion("CT");

			user1.setPlayer(player1);

			userRepository.save(user1);

			User user2 = new User();
			user2.setUsername("ahmed123");
			user2.setPassword("mohammagoat123!");
			user2.setRole(Role.ROLE_PLAYER);
			user2.setEmail("admin1@gmail.com");
			Player player2 = new Player();
			player2.setPhoneNumber("23432");
			player2.setPostion("CT");

			user2.setPlayer(player2);

			userRepository.save(user2);

			User user3 = new User();
			user3.setUsername("123ahmed");
			user3.setPassword("mohammagoat123!");
			user3.setRole(Role.ROLE_PLAYER);
			user3.setEmail("admin2@gmail.com");
			Player player3 = new Player();
			player3.setPhoneNumber("23432");
			player3.setPostion("CT");

			user3.setPlayer(player3);

			player3.getFavorites().add(player2);

			userRepository.save(user3);

			Session session = new Session();
			session.setPrice(2);
			session.setType("Boom");
			session.setMatchDateTime(LocalDateTime.now().plusMonths(1));
			session.setMaxPlayers(12);

			Court court = new Court();
			court.setCity("ss");
			court.setName("Court 1");
			court.setPlaceId("d");

			courtRepository.save(court);

			session.setCourt(court);

			sessionRepository.save(session);

			System.out.println("Server running on port " + serverPort);
		};
	}

}
