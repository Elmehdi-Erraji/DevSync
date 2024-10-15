package domain;

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
    private String username;
    private Long taskId;
    private String previousAssignedUser;
    private String newAssignedUser;
    private String managerApproved;
    private LocalDateTime dateUsed;

    @Override
    public String toString() {
        return "TokenLog{" +
                "id=" + id +
                ", tokensUsed=" + tokensUsed +
                ", action='" + action + '\'' +
                ", username='" + username + '\'' +
                ", taskId=" + taskId +
                ", previousAssignedUser='" + previousAssignedUser + '\'' +
                ", newAssignedUser='" + newAssignedUser + '\'' +
                ", managerApproved='" + managerApproved + '\'' +
                ", dateUsed=" + dateUsed +
                '}';
    }
}
