package com.ballersApi.ballersApi;

import com.ballersApi.ballersApi.models.*;
import com.ballersApi.ballersApi.repositories.PlayerRepository;
import com.ballersApi.ballersApi.repositories.SessionRepository;
import com.ballersApi.ballersApi.repositories.SessionTeamRepository;
import com.ballersApi.ballersApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalTime;

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
			, PlayerRepository playerRepository
			, SessionRepository sessionRepository
			, SessionTeamRepository sessionTeamRepository
	) {
		return args -> {

			User user = new User();
			user.setUsername("admighjgjn");
			user.setPassword("123");
			user.setEmail("admin@gmail.com");
			user.setRole(Role.ROLE_PLAYER);

			Player player = new Player();
			user.setPlayer(player);
			player.setPostion("Guard");
			player.setPhoneNumber("1234567890");


			userRepository.save(user);

			Session session = new Session();
			session.setPlayerCount(3);
			session.setPrice(8);
			session.setType(SessionType.Random);
			session.setMatchDate(LocalDate.now().plusDays(3));
			session.setMaxPlayers(10);
			session.setMatchStartTime(LocalTime.now().plusHours(4));
			session.setMatchEndTime(LocalTime.now().plusHours(6));
			sessionRepository.save(session);

			Session session1 = new Session();
			session1.setPlayerCount(0);
			session1.setPrice(10);
			session1.setType(SessionType.Teams);
			session1.setMatchDate(LocalDate.now().plusDays(3));
			session1.setMaxPlayers(10);
			session1.setMatchStartTime(LocalTime.now().plusHours(2));
			session1.setMatchEndTime(LocalTime.now().plusHours(4));
			sessionRepository.save(session1);

			Session session2 = new Session();
			session2.setPlayerCount(5);
			session2.setPrice(10);
			session2.setType(SessionType.Teams);
			session2.setMatchDate(LocalDate.now().plusDays(4));
			session2.setMaxPlayers(10);
			session2.setMatchStartTime(LocalTime.now().plusHours(2));
			session2.setMatchEndTime(LocalTime.now().plusHours(4));
			sessionRepository.save(session2);
			
			Session session3 = new Session();
			session3.setPlayerCount(9);
			session3.setPrice(8);
			session3.setType(SessionType.Random);
			session3.setMatchDate(LocalDate.now().plusDays(5));
			session3.setMaxPlayers(10);
			session3.setMatchStartTime(LocalTime.now().plusHours(2));
			session3.setMatchEndTime(LocalTime.now().plusHours(4));
			sessionRepository.save(session3);

			Session session4 = new Session();
			session4.setPlayerCount(10);
			session4.setPrice(8);
			session4.setType(SessionType.Random);
			session4.setMatchDate(LocalDate.now().plusDays(2));
			session4.setMaxPlayers(10);
			session4.setMatchStartTime(LocalTime.now().plusHours(2));
			session4.setMatchEndTime(LocalTime.now().plusHours(4));
			sessionRepository.save(session4);

			System.out.println("Server running on port " + serverPort);
		};
	}

}
