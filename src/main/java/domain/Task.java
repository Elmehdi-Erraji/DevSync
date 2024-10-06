package domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import domain.enums.TaskStatus;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String dueDate;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;// NEW, IN_PROGRESS, DONE

    private boolean isRefused = false;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator; // The user or manager who created the task

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser; // The user responsible for completing the task

    @ManyToMany
    @JoinTable(
            name = "task_tag",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Request> requests;


}
