package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import domain.Task;


import java.util.List;

public class TaskRepository {
    private EntityManager entityManager;

    public TaskRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Task> findAllTasks() {
        Query query = entityManager.createQuery("SELECT t FROM Task t", Task.class);
        return (List<Task>) ((TypedQuery<?>) query).getResultList();
    }

    public Task findTaskById(Long id) {
        return entityManager.find(Task.class, id);
    }

    public void insertTask(Task task) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(task);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void updateTask(Task task) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(task);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void deleteTask(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Task task = findTaskById(id);
            if (task != null) {
                entityManager.remove(task);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }


    public List<Task> findTasksAssignedToUser(Long userId) {
        TypedQuery<Task> query = entityManager.createQuery(
                "SELECT t FROM Task t WHERE t.assignedUser.id = :userId AND t.creator.id != :userId", Task.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<Task> findTasksCreatedByUser(Long userId) {
        TypedQuery<Task> query = entityManager.createQuery(
                "SELECT t FROM Task t WHERE t.assignedUser.id = :userId AND t.creator.id = :userId", Task.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
