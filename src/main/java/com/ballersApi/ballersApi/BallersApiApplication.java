package com.ballersApi.ballersApi;


import com.ballersApi.ballersApi.models.*;
import com.ballersApi.ballersApi.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
            InvitationRepository invitationRepository,
            PlayerRepository playerRepository,
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
            session.setPlayerCount(0);
            session.setPrice(8);
            session.setType(SessionType.Random);
            session.setMatchDate(LocalDate.now().plusDays(1));
            session.setMaxPlayers(10);
            session.setMatchStartTime(LocalTime.now().plusHours(3));
            session.setMatchEndTime(LocalTime.now().plusHours(5));
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
            session1.setPlayerCount(0);
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


            User adminUser = new User();
            adminUser.setUsername("adminABC");
            adminUser.setPassword(passwordEncoder.encode("password98!"));
            adminUser.setRole(Role.ROLE_ADMIN);
            adminUser.setEmail("admin2w@gmail.com");

            userRepository.save(adminUser);

            User user1 = new User();
            user1.setUsername("waleeeeeeed");
            user1.setPassword(passwordEncoder.encode("mohammagoat123!"));
            user1.setRole(Role.ROLE_PLAYER);
            user1.setEmail("admin4@gmail.com");
            Player player1 = new Player();
            player1.setPhoneNumber("23432");
            player1.setPostion("CT");
            player1.setVerified(true);
            player1.setVerified(true);
            user1.setPlayer(player1);

            userRepository.save(user1);

            User user2 = new User();
            user2.setUsername("Issaaaaaaaaa");
            user2.setPassword("mohammagoat123!");
            user2.setRole(Role.ROLE_PLAYER);
            user2.setEmail("admin1@gmail.com");
            Player player2 = new Player();
            player2.setPhoneNumber("23432");
            player2.setPostion("CT");

            user2.setPlayer(player2);

            userRepository.save(user2);



            User user6 = new User();
            user6.setUsername("Loaiiiiii");
            user6.setPassword("mohammagoat123!");
            user6.setRole(Role.ROLE_PLAYER);
            user6.setEmail("admin19@gmail.com");
            Player player5 = new Player();
            player5.setPhoneNumber("234362");
            player5.setPostion("PG");

            user6.setPlayer(player5);

            userRepository.save(user6);

            User user7 = new User();
            user7.setUsername("Zaaaaaaaaaaaid");
            user7.setPassword("mohammagoat123!");
            user7.setRole(Role.ROLE_PLAYER);
            user7.setEmail("admin6@gmail.com");
            Player player6 = new Player();
            player6.setPhoneNumber("23432");
            player6.setPostion("SG");

            user7.setPlayer(player6);

            userRepository.save(user7);

            User user8 = new User();
            user8.setUsername("Amrooooo");
            user8.setPassword("mohammagoat123!");
            user8.setRole(Role.ROLE_PLAYER);
            user8.setEmail("admin7@gmail.com");
            Player player7 = new Player();
            player7.setPhoneNumber("23432");
            player7.setPostion("SF");

            user8.setPlayer(player7);

            userRepository.save(user8);

            User user9 = new User();
            user9.setUsername("Omaaaaaaaaar");
            user9.setPassword("mohammagoat123!");
            user9.setRole(Role.ROLE_PLAYER);
            user9.setEmail("admin8@gmail.com");
            Player player8 = new Player();
            player8.setPhoneNumber("23432");
            player8.setPostion("PF");

            user9.setPlayer(player8);

            userRepository.save(user9);


            User user10 = new User();
            user10.setUsername("Aliiiiiiiiii");
            user10.setPassword("mohammagoat123!");
            user10.setRole(Role.ROLE_PLAYER);
            user10.setEmail("admin9@gmail.com");
            Player player9 = new Player();
            player9.setPhoneNumber("23432");
            player9.setPostion("CT");

            user10.setPlayer(player9);

            userRepository.save(user10);

            User user11 = new User();
            user11.setUsername("Murtadaaaaaaaaaa");
            user11.setPassword("mohammagoat123!");
            user11.setRole(Role.ROLE_PLAYER);
            user11.setEmail("admin10@gmail.com");
            Player player10 = new Player();
            player10.setPhoneNumber("23432");
            player10.setPostion("C");

            user11.setPlayer(player10);

            userRepository.save(user11);



            User user3 = new User();
            user3.setUsername("farisssssss");
            user3.setPassword("mohammagoat123!");
            user3.setRole(Role.ROLE_PLAYER);
            user3.setEmail("admin2@gmail.com");
            Player player3 = new Player();
            player3.setPhoneNumber("23432");
            player3.setPostion("CT");

            player3.getFavorites().add(player10);

            user3.setPlayer(player3);


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
            user4.setPassword(passwordEncoder.encode("mohammagoat123!"));
            user4.setRole(Role.ROLE_REFEREE);
            user4.setEmail("referee@gmail.com");

            userRepository.save(user4);

            User user5 = new User();
            user5.setUsername("3bdDaGoat");
            user5.setPassword("mohammagoat123!");
            user5.setRole(Role.ROLE_PLAYER);
            user5.setEmail("admin3@gmail.com");
            Player player4 = new Player();
            player4.setPhoneNumber("23432");
            player4.setPostion("RB");


            user5.setPlayer(player4);


            userRepository.save(user5);





            Session session2 = new Session();
            session2.setPlayerCount(10);
            session2.setPrice(8);
            session2.setType(SessionType.Random);
            session2.setMatchDate(LocalDate.now().plusDays(3));
            session2.setMaxPlayers(10);
            session2.setReferee(user4);

            session2.setMatchStartTime(LocalTime.now().plusHours(2));
            session2.setMatchEndTime(LocalTime.now().plusHours(4));
            SessionTeam sessionTeam4 = new SessionTeam();
            SessionTeam sessionTeam5 = new SessionTeam();
            sessionRepository.save(session2);
            sessionTeamRepository.save(sessionTeam4);
            sessionTeamRepository.save(sessionTeam5);
            session2.setTeamA(sessionTeam4);
            session2.setTeamB(sessionTeam5);

            player10.getSessionTeams().add(sessionTeam4);
            sessionTeamRepository.save(sessionTeam4);

            player8.getSessionTeams().add(sessionTeam4);
            sessionTeamRepository.save(sessionTeam4);

            player9.getSessionTeams().add(sessionTeam4);
            sessionTeamRepository.save(sessionTeam4);

            player1.getSessionTeams().add(sessionTeam4);
            sessionTeamRepository.save(sessionTeam4);

            player2.getSessionTeams().add(sessionTeam4);
            sessionTeamRepository.save(sessionTeam4);

            player3.getSessionTeams().add(sessionTeam5);
            sessionTeamRepository.save(sessionTeam5);

            player4.getSessionTeams().add(sessionTeam5);
            sessionTeamRepository.save(sessionTeam5);

            player5.getSessionTeams().add(sessionTeam5);
            sessionTeamRepository.save(sessionTeam5);

            player6.getSessionTeams().add(sessionTeam5);
            sessionTeamRepository.save(sessionTeam5);

            player7.getSessionTeams().add(sessionTeam5);
            sessionTeamRepository.save(sessionTeam5);


            sessionTeamRepository.save(sessionTeam4);
            sessionTeamRepository.save(sessionTeam5);
            sessionRepository.save(session2);
            Court court6 = new Court();
            court6.setCity("Amman");
            court6.setName("Orthodox Club");
            court6.setHasBathroom(true);
            court6.setPlaceId("WVWW+XH Amman");
            court6.setHasParking(true);
            court6.setHasCafeteria(false);
            court6.setHasBathroom(true);
            sessionRepository.save(session2);
            sessionTeamRepository.save(sessionTeam4);
            sessionTeamRepository.save(sessionTeam5);
            courtRepository.save(court6);
            session2.setCourt(court6);
            session2.setTeamA(sessionTeam4);
            session2.setTeamB(sessionTeam5);
            courtRepository.save(court6);
            sessionTeamRepository.save(sessionTeam4);
            sessionTeamRepository.save(sessionTeam5);
            sessionRepository.save(session2);

            Session session3 = new Session();
            session3.setPlayerCount(1);
            session3.setPrice(8);
            session3.setType(SessionType.Random);
            session3.setMatchDate(LocalDate.now().plusDays(3));
            session3.setMaxPlayers(10);
            session3.setMatchStartTime(LocalTime.now().plusHours(2));
            session3.setMatchEndTime(LocalTime.now().plusHours(4));
            SessionTeam sessionTeam2 = new SessionTeam();
            SessionTeam sessionTeam3 = new SessionTeam();
            player8.getSessionTeams().add(sessionTeam2);
            sessionTeamRepository.save(sessionTeam2);

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


            Session session5 = new Session();
            session5.setPlayerCount(5);
            session5.setPrice(8);
            session5.setType(SessionType.Teams);
            session5.setReferee(user4);

            session5.setMatchDate(LocalDate.now().plusDays(2));
            session5.setMaxPlayers(10);
            session5.setMatchStartTime(LocalTime.now().plusHours(2));
            session5.setMatchEndTime(LocalTime.now().plusHours(4));
            SessionTeam sessionTeam11 = new SessionTeam();
            SessionTeam sessionTeam12 = new SessionTeam();

            player10.getSessionTeams().add(sessionTeam11);
            sessionTeamRepository.save(sessionTeam11);
            player3.getSessionTeams().add(sessionTeam11);
            sessionTeamRepository.save(sessionTeam11);
            player4.getSessionTeams().add(sessionTeam11);
            sessionTeamRepository.save(sessionTeam11);

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

            Session session4 = new Session();
            session4.setPlayerCount(5);
            session4.setPrice(8);
            session4.setReferee(user4);
            session4.setType(SessionType.Random);
            session4.setMatchDate(LocalDate.now().plusDays(1));
            session4.setMaxPlayers(10);
            session4.setMatchStartTime(LocalTime.now().plusHours(2));
            session4.setMatchEndTime(LocalTime.now().plusHours(4));
            SessionTeam sessionTeam = new SessionTeam();
            SessionTeam sessionTeam1 = new SessionTeam();
            player8.getSessionTeams().add(sessionTeam);
            sessionTeamRepository.save(sessionTeam);

            player10.getSessionTeams().add(sessionTeam);
            sessionTeamRepository.save(sessionTeam);
            player3.getSessionTeams().add(sessionTeam);
            sessionTeamRepository.save(sessionTeam);
            player4.getSessionTeams().add(sessionTeam);
            sessionTeamRepository.save(sessionTeam);
            player1.getSessionTeams().add(sessionTeam);

            playerRepository.save(player1);
            playerRepository.save(player2);
            playerRepository.save(player5);
            playerRepository.save(player6);
            playerRepository.save(player7);
            playerRepository.save(player8);
            playerRepository.save(player9);
            playerRepository.save(player10);
            playerRepository.save(player3);
            playerRepository.save(player4);


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

            Invitation invite = new Invitation();
            invite.setPlayer(player1);
            invite.setReceiver(player);
            invite.setSession(session);
            invite.setCreatedAt(LocalDateTime.now());
            invite.setStatus(false); // pending

            invitationRepository.save(invite);

            Invitation invite2 = new Invitation();
            invite2.setPlayer(player10);
            invite2.setReceiver(player);
            invite2.setSession(session);
            invite2.setCreatedAt(LocalDateTime.now());
            invite2.setStatus(false); // pending

            invitationRepository.save(invite2);


            Invitation invite3 = new Invitation();
            invite3.setPlayer(player1);
            invite3.setReceiver(player);
            invite3.setSession(session1);
            invite3.setCreatedAt(LocalDateTime.now());
            invite3.setStatus(false); // pending

            invitationRepository.save(invite3);


            playerRepository.save(player);

            System.out.println("Server running on port " + serverPort);
        };
    }

}
