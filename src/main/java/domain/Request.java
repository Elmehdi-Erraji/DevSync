package domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import domain.enums.RequestStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private LocalDateTime requestDate;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



}
