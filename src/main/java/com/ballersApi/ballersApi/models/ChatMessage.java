package com.ballersApi.ballersApi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String message;

    private LocalDateTime timeStamp=LocalDateTime.now();

    private MessageType type;
    // @ManyToOne
    // @JoinColumn(name = "player_id",nullable = true)//added just tot test message remove after
    // private Player sender;
    //private String sender;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;
}
