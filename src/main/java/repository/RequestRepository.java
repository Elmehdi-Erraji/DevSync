package repository;

import domain.Request;
import domain.User;
import domain.enums.RequestStatus;
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

    public Optional<Request> findById(Long id) {
        Request request = entityManager.find(Request.class, id);
        return Optional.ofNullable(request);
    }

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

    public void updateRequest(Request request) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (request.getId() != null) {
                entityManager.merge(request);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void updateRequestStatus(Long requestId, RequestStatus status) {
        entityManager.getTransaction().begin();
        Request request = entityManager.find(Request.class, requestId);
        if (request != null) {
            request.setStatus(status);
            entityManager.merge(request);
        }
        entityManager.getTransaction().commit();
    }

    public List<Request> findAllPendingRequests() {
        TypedQuery<Request> query = entityManager.createQuery(
                "SELECT r FROM Request r WHERE r.status = :status", Request.class);
        query.setParameter("status", RequestStatus.PENDING);
        return query.getResultList();
    }

}
