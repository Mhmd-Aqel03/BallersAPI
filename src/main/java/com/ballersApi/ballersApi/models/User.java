package com.ballersApi.ballersApi.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty(message = "Username can't be empty")
    @Size(min = 7, max = 20,message = "Username must be between 7-20 characters long")
    @Column(unique = true,nullable = false)
    private String username;

    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Email is not valid")
    @Column(unique = true,nullable = false)
    private String email;
    @NotEmpty(message = "Password can't be empty")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "referee_id")
    private Referee referee;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private Player player;
}
