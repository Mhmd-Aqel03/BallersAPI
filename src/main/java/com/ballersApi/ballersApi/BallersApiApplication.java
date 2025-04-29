package com.ballersApi.ballersApi;


import com.ballersApi.ballersApi.models.*;
import com.ballersApi.ballersApi.repositories.CourtRepository;
import com.ballersApi.ballersApi.repositories.SessionRepository;
import com.ballersApi.ballersApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;

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
            CourtRepository courtRepository,
            PasswordEncoder passwordEncoder

    ) {
        return args -> {
            // Users for testing
            User user = new User();
            user.setUsername("username");
            user.setPassword(passwordEncoder.encode("password98!"));
            user.setRole(Role.ROLE_ADMIN);
            user.setEmail("admin@gmail.com");
            Player player = new Player();
            player.setPhoneNumber("23432");
            player.setPostion("CT");
            player.setVerified(true);

            user.setPlayer(player);

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

            User adminUser = new User();
            adminUser.setUsername("adminABC");
            adminUser.setPassword(passwordEncoder.encode("password98!"));
            adminUser.setRole(Role.ROLE_ADMIN);
            adminUser.setEmail("admin2w@gmail.com");

            userRepository.save(adminUser);

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


            Court court = new Court();
            court.setCity("Ajloun");
            court.setName("Ajloun Court");
            court.setPlaceId("ChIJ7_H6QjqeHBURRyJte_2Qunw");

            courtRepository.save(court);

            session.setCourt(court);

            sessionRepository.save(session);


            Court court2 = new Court();
            court2.setCity("Amman");
            court2.setName("Al-Shafa Court");
            court2.setPlaceId("ChIJ7_H6QjqeHBURRyJte_2Qunw");

            courtRepository.save(court2);

            session1.setCourt(court2);

            sessionRepository.save(session1);

            User user4 = new User();
            user4.setUsername("referee");
            user4.setPassword("mohammagoat123!");
            user4.setRole(Role.ROLE_REFEREE);
            user4.setEmail("referee@gmail.com");

            userRepository.save(user4);


            System.out.println("Server running on port " + serverPort);
        };
    }

}
