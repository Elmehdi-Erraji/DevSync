package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import domain.Task;
import repository.TaskRepository;

import java.util.List;

public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("your-persistence-unit-name");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        this.taskRepository = new TaskRepository(entityManager);
    }

    public void insertTask(Task task) {
        taskRepository.insertTask(task);
    }

    public void updateTask(Task task) {
        taskRepository.updateTask(task);
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteTask(taskId);
    }

    public Task findTaskById(Long taskId) {
        return taskRepository.findTaskById(taskId);
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAllTasks();
    }

    public List<Task> findTasksAssignedToUser(Long userId) {
        return taskRepository.findTasksAssignedToUser(userId);
    }

    public List<Task> findTasksCreatedByUser(Long userId) {
        return taskRepository.findTasksCreatedByUser(userId);
    }
}
