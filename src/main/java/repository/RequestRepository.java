package repository;

import domain.Request;
import domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class RequestRepository {

    private EntityManager entityManager;

    public RequestRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Save or update a request
    public Request save(Request request) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (request.getId() == null) {
                entityManager.persist(request);
            } else {
                entityManager.merge(request);
            }
            transaction.commit();
            return request;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Find request by ID
    public Optional<Request> findById(Long id) {
        Request request = entityManager.find(Request.class, id);
        return Optional.ofNullable(request);
    }

    // Delete request by ID
    public void delete(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Request request = entityManager.find(Request.class, id);
            if (request != null) {
                entityManager.remove(request);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error while deleting the request: " + e.getMessage(), e);
        }
    }
    public List<Request> findAll() {
        TypedQuery<Request> query = entityManager.createQuery("SELECT r FROM Request r", Request.class);
        return query.getResultList();
    }

}
