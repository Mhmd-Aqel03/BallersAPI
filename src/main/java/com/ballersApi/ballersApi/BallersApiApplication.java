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
            PasswordEncoder passwordEncoder,
        SessionTeamRepository sessionTeamRepository

    ) {
        return args -> {
            // Users for testing
            User user = new User();
            user.setUsername("username");
            user.setPassword(passwordEncoder.encode("password98!"));
            user.setRole(Role.ROLE_PLAYER);
            user.setEmail("admin@gmail.com");
            Player player = new Player();
            player.setPhoneNumber("23432");
            player.setPostion("CT");
            player.setVerified(true);

            user.setPlayer(player);

            userRepository.save(user);

           
			Session session = new Session();
			session.setPlayerCount(4);
			session.setPrice(8);
			session.setType(SessionType.Random);
			session.setMatchDate(LocalDate.now().plusDays(1));
			session.setMaxPlayers(10);
			session.setMatchStartTime(LocalTime.now().plusHours(2));
			session.setMatchEndTime(LocalTime.now().plusHours(4));
			SessionTeam sessionTeam8 = new SessionTeam();
			SessionTeam sessionTeam9 = new SessionTeam();
			sessionRepository.save(session);
			sessionTeamRepository.save(sessionTeam8);
			sessionTeamRepository.save(sessionTeam9);
			session.setTeamA(sessionTeam8);
			session.setTeamB(sessionTeam9);
			sessionTeamRepository.save(sessionTeam8);
			sessionTeamRepository.save(sessionTeam9);
			sessionRepository.save(session);


			Session session1 = new Session();
			session1.setPlayerCount(5);
			session1.setPrice(8);
			session1.setType(SessionType.Random);
			session1.setMatchDate(LocalDate.now().plusDays(1));
			session1.setMaxPlayers(10);
			session1.setMatchStartTime(LocalTime.now().plusHours(2));
			session1.setMatchEndTime(LocalTime.now().plusHours(4));
			SessionTeam sessionTeam6 = new SessionTeam();
			SessionTeam sessionTeam7 = new SessionTeam();
			sessionRepository.save(session1);
			sessionTeamRepository.save(sessionTeam6);
			sessionTeamRepository.save(sessionTeam7);
			session1.setTeamA(sessionTeam6);
			session1.setTeamB(sessionTeam7);
			sessionTeamRepository.save(sessionTeam6);
			sessionTeamRepository.save(sessionTeam7);
			sessionRepository.save(session1);

			Session session2 = new Session();
			session2.setPlayerCount(10);
			session2.setPrice(8);
			session2.setType(SessionType.Random);
			session2.setMatchDate(LocalDate.now().plusDays(3));
			session2.setMaxPlayers(10);
			session2.setMatchStartTime(LocalTime.now().plusHours(2));
			session2.setMatchEndTime(LocalTime.now().plusHours(4));
			SessionTeam sessionTeam4 = new SessionTeam();
			SessionTeam sessionTeam5 = new SessionTeam();
			sessionRepository.save(session2);
			sessionTeamRepository.save(sessionTeam4);
			sessionTeamRepository.save(sessionTeam5);
			session2.setTeamA(sessionTeam4);
			session2.setTeamB(sessionTeam5);
			sessionTeamRepository.save(sessionTeam4);
			sessionTeamRepository.save(sessionTeam5);
			sessionRepository.save(session2);

			Session session3 = new Session();
			session3.setPlayerCount(0);
			session3.setPrice(8);
			session3.setType(SessionType.Random);
			session3.setMatchDate(LocalDate.now().plusDays(3));
			session3.setMaxPlayers(10);
			session3.setMatchStartTime(LocalTime.now().plusHours(2));
			session3.setMatchEndTime(LocalTime.now().plusHours(4));
			SessionTeam sessionTeam2 = new SessionTeam();
			SessionTeam sessionTeam3 = new SessionTeam();
			Court court3 = new Court();
			court3.setCity("Amman");
			court3.setName("Trax Jo");
			court3.setHasBathroom(true);
			court3.setPlaceId("XRGQ+7C Amman");
			court3.setHasParking(false);
			court3.setHasCafeteria(true);
			court3.setHasBathroom(true);
			sessionRepository.save(session3);
			sessionTeamRepository.save(sessionTeam2);
			sessionTeamRepository.save(sessionTeam3);
			courtRepository.save(court3);
			session3.setCourt(court3);
			session3.setTeamA(sessionTeam2);
			session3.setTeamB(sessionTeam3);
			courtRepository.save(court3);
			sessionTeamRepository.save(sessionTeam2);
			sessionTeamRepository.save(sessionTeam3);
			sessionRepository.save(session3);

			Session session4 = new Session();
			session4.setPlayerCount(0);
			session4.setPrice(8);
			session4.setType(SessionType.Random);
			session4.setMatchDate(LocalDate.now().plusDays(3));
			session4.setMaxPlayers(10);
			session4.setMatchStartTime(LocalTime.now().plusHours(2));
			session4.setMatchEndTime(LocalTime.now().plusHours(4));
			SessionTeam sessionTeam = new SessionTeam();
			SessionTeam sessionTeam1 = new SessionTeam();
			Court court4 = new Court();
			court4.setCity("Amman");
			court4.setName("Trax Jo");
			court4.setHasBathroom(true);
			court4.setPlaceId("XRGQ+7C Amman");
			court4.setHasParking(false);
			court4.setHasCafeteria(true);
			court4.setHasBathroom(true);
			sessionRepository.save(session4);
			sessionTeamRepository.save(sessionTeam);
			sessionTeamRepository.save(sessionTeam1);
			courtRepository.save(court4);
			session4.setCourt(court4);
			session4.setTeamA(sessionTeam);
			session4.setTeamB(sessionTeam1);
			courtRepository.save(court4);
			sessionTeamRepository.save(sessionTeam);
			sessionTeamRepository.save(sessionTeam1);
			sessionRepository.save(session4);

			Session session5 = new Session();
			session5.setPlayerCount(0);
			session5.setPrice(8);
			session5.setType(SessionType.Teams);
			session5.setMatchDate(LocalDate.now().plusDays(2));
			session5.setMaxPlayers(10);
			session5.setMatchStartTime(LocalTime.now().plusHours(2));
			session5.setMatchEndTime(LocalTime.now().plusHours(4));
			SessionTeam sessionTeam11 = new SessionTeam();
			SessionTeam sessionTeam12 = new SessionTeam();
			Court court5 = new Court();
			court5.setCity("Amman");
			court5.setName("Trax Jo");
			court5.setHasBathroom(true);
			court5.setPlaceId("XRGQ+7C Amman");
			court5.setHasParking(false);
			court5.setHasCafeteria(true);
			court5.setHasBathroom(true);
			sessionRepository.save(session5);
			sessionTeamRepository.save(sessionTeam11);
			sessionTeamRepository.save(sessionTeam12);
			courtRepository.save(court5);
			session5.setCourt(court5);
			session5.setTeamA(sessionTeam11);
			session5.setTeamB(sessionTeam12);
			courtRepository.save(court5);
			sessionTeamRepository.save(sessionTeam11);
			sessionTeamRepository.save(sessionTeam12);
			sessionRepository.save(session5);

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
