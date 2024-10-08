package domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import domain.enums.Role;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "users") // Change to a non-reserved name
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role; // MANAGER or USER

    private int dailyTokens = 2;

    private int monthlyTokens = 1;

    // Relationships
    @OneToMany(mappedBy = "assignedUser", cascade = CascadeType.ALL)
    private List<Task> assignedTasks;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Task> createdTasks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TokenLog> tokenLogs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Request> requests;
}
