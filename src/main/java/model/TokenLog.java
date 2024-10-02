package model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class TokenLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int tokensUsed;
    private String action;
    private LocalDateTime dateUsed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
