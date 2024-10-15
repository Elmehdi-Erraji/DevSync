package service;

import domain.Task;
import exception.TaskException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import repository.TaskRepository;
import java.util.Arrays;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("your-persistence-unit-name");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        this.taskRepository = new TaskRepository(entityManager);
    }

    public void insertTask(Task task) {
        validateTask(task);
        taskRepository.insertTask(task);
    }

    public void updateTask(Task task) {
        if (task.getId() == null) {
            throw new TaskException("Task ID cannot be null when updating a task.");
        }
        validateTask(task);
        taskRepository.updateTask(task);
    }

    public void deleteTask(Long taskId) {
        if (taskId == null) {
            throw new TaskException("Task ID cannot be null.");
        }
        Task task = taskRepository.findTaskById(taskId);
        if (task == null) {
            throw new TaskException("Task not found with ID: " + taskId);
        }
        taskRepository.deleteTask(taskId);
    }

    public Task findTaskById(Long taskId) {
        if (taskId == null) {
            throw new TaskException("Task ID cannot be null.");
        }
        Task task = taskRepository.findTaskById(taskId);
        if (task == null) {
            throw new TaskException("Task not found with ID: " + taskId);
        }
        return task;
    }

    public List<Task> findAllTasks() {
        List<Task> tasks = taskRepository.findAllTasks();
        return tasks;
    }

    public List<Task> findTasksAssignedToUser(Long userId) {
        validateUserId(userId);
        return taskRepository.findTasksAssignedToUser(userId);
    }

    public List<Task> findTasksCreatedByUser(Long userId) {
        validateUserId(userId);
        return taskRepository.findTasksCreatedByUser(userId);
    }

    public List<Task> filterTasks(String[] tagIds, LocalDate startDate, LocalDate dueDate) {
        List<Task> tasks = findAllTasks();

        // Filter by tags if tagIds are provided
        if (tagIds != null && tagIds.length > 0) {
            tasks = tasks.stream()
                    .filter(task -> task.getTags().stream()
                            .anyMatch(tag -> Arrays.asList(tagIds).contains(String.valueOf(tag.getId()))))
                    .collect(Collectors.toList());
        }

        // Filter by start date if provided
        if (startDate != null) {
            tasks = tasks.stream()
                    .filter(task -> !task.getStartDate().isBefore(startDate))
                    .collect(Collectors.toList());
        }

        // Filter by due date if provided
        if (dueDate != null) {
            tasks = tasks.stream()
                    .filter(task -> {
                        LocalDate taskDueDate = LocalDate.parse(task.getDueDate(), DateTimeFormatter.ISO_DATE);
                        return !taskDueDate.isAfter(dueDate);
                    })
                    .collect(Collectors.toList());
        }

        return tasks;
    }


    private void validateTask(Task task) {
        if (task == null) {
            throw new TaskException("Task cannot be null.");
        }
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new TaskException("Task title cannot be null or empty.");
        }
        if (task.getTitle().length() > 100) {
            throw new TaskException("Task title cannot exceed 100 characters.");
        }
        if (task.getDescription() != null && task.getDescription().length() > 500) {
            throw new TaskException("Task description cannot exceed 500 characters.");
        }
    }

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new TaskException("User ID cannot be null.");
        }
    }

}
