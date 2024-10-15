package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Data
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotEmpty(message = "Tag name is required")
    @Size(min = 3, max = 20, message = "Tag name must be between 3 and 20 characters")
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Task> tasks;


}
